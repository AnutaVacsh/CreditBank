package ru.vaschenko.deal.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vaschenko.deal.models.Client;
import ru.vaschenko.deal.models.Statement;
import ru.vaschenko.deal.models.enams.ApplicationStatus;
import ru.vaschenko.deal.repositories.StatementRepositories;

@ExtendWith(MockitoExtension.class)
class StatementServiceTest {

  @Mock private StatementRepositories statementRepositories;

  @InjectMocks private StatementService statementService;

  private Client client;
  private Statement statement;
  private UUID statementId;

  @BeforeEach
  void setUp() {
    Client client = new Client()
            .setEmail("john.doe@example.com")
            .setFirstName("John")
            .setLastName("Due");

    Statement statement = new Statement()
            .setClient(client)
            .setCreationDate(java.time.LocalDateTime.now());


    statementId = UUID.randomUUID();
  }

  @Test
  void testCreateStatement() {
    when(statementRepositories.save(any(Statement.class))).thenReturn(statement);

    Statement createdStatement = statementService.createStatement(client);

    ArgumentCaptor<Statement> captor = ArgumentCaptor.forClass(Statement.class);
    verify(statementRepositories).save(captor.capture());

    Statement capturedStatement = captor.getValue();
    assertEquals(ApplicationStatus.PREAPPROVAL, capturedStatement.getStatus());
    assertEquals(client, capturedStatement.getClient());
  }

  @Test
  void testSaveStatement() {
    when(statementRepositories.save(statement)).thenReturn(statement);

    Statement savedStatement = statementService.saveStatement(statement);

    assertNotNull(savedStatement);
    assertEquals(statement, savedStatement);

    verify(statementRepositories).save(statement);
  }

  @Test
  void testFindStatementById() {
    when(statementRepositories.findById(statementId)).thenReturn(Optional.of(statement));

    // Вызов метода
    Statement foundStatement = statementService.findStatementById(statementId);

    assertNotNull(foundStatement);
    assertEquals(statement, foundStatement);

    verify(statementRepositories).findById(statementId);
  }

  @Test
  void testFindStatementById_NotFound() {
    when(statementRepositories.findById(statementId)).thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class, () -> statementService.findStatementById(statementId));

    verify(statementRepositories).findById(statementId);
  }
}
