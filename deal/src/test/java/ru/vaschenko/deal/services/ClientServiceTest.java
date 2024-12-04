package ru.vaschenko.deal.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vaschenko.deal.dto.LoanStatementRequestDto;
import ru.vaschenko.deal.mapping.ClientMapper;
import ru.vaschenko.deal.models.Client;
import ru.vaschenko.deal.repositories.ClientRepositories;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

  @Mock private ClientRepositories clientRepositories;

  @Mock private ClientMapper clientMapper;

  @InjectMocks private ClientService clientService;

  private LoanStatementRequestDto loanStatementRequestDto;
  private Client client;

  @BeforeEach
  void setUp() {

    loanStatementRequestDto =
        LoanStatementRequestDto.builder()
            .email("john.doe@example.com")
            .firstName("John")
            .lastName("Doe")
            .build();

    client =
        Client.builder().email("john.doe@example.com").firstName("John").lastName("Doe").build();
  }

  @Test
  void testCreateClient() {
    when(clientMapper.toClient(loanStatementRequestDto)).thenReturn(client);
    when(clientRepositories.save(client)).thenReturn(client);

    Client createdClient = clientService.createClient(loanStatementRequestDto);

    assertNotNull(createdClient);
    assertEquals("John", createdClient.getFirstName());
    assertEquals("Doe", createdClient.getLastName());
    assertEquals("john.doe@example.com", createdClient.getEmail());

    verify(clientMapper).toClient(loanStatementRequestDto);
    verify(clientRepositories).save(client);
  }
}
