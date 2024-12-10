package ru.vaschenko.deal.services;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.deal.models.Client;
import ru.vaschenko.deal.models.Statement;
import ru.vaschenko.deal.models.enams.ApplicationStatus;
import ru.vaschenko.deal.repositories.StatementRepositories;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatementService {
  private final StatementRepositories statementRepositories;

  /**
   * Создаёт новую заявку на основе переданного клиента. Устанавливает начальный статус заявки как
   * PREAPPROVAL и сохраняет её в базе данных.
   *
   * @param client объект клиента, связанный с заявкой {@link Client}.
   * @return созданная и сохранённая заявка {@link Statement}.
   */
  protected Statement createStatement(Client client) {
    Statement statement = new Statement().setClient(client).setCreationDate(LocalDateTime.now());
    statement.setStatus(ApplicationStatus.PREAPPROVAL);
    return saveStatement(statement);
  }

  /**
   * Сохраняет переданную заявку в базе данных.
   *
   * @param statement объект заявки {@link Statement}.
   * @return сохранённая заявка.
   */
  protected Statement saveStatement(Statement statement) {
    log.debug("save statement: {}", statement);
    return statementRepositories.save(statement);
  }

  /**
   * Ищет заявку по идентификатору.
   *
   * @param id уникальный идентификатор заявки {@link UUID}.
   * @return найденная заявка {@link Statement}.
   * @throws EntityNotFoundException если заявка с указанным идентификатором не найдена.
   */
  protected Statement findStatementById(UUID id) {
    log.debug("find statement by ID: {}", id);
    return statementRepositories
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Заявка не найдена с ID: " + id));
  }
}
