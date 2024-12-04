package ru.vaschenko.deal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.deal.dto.LoanStatementRequestDto;
import ru.vaschenko.deal.mapping.ClientMapper;
import ru.vaschenko.deal.models.Client;
import ru.vaschenko.deal.repositories.ClientRepositories;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {
  private final ClientRepositories clientRepositories;
  private final ClientMapper clientMapper;

  /**
   * Создание клиента на основе LoanStatementRequestDto и сохранение в бд.
   *
   * @param loanStatementRequestDto объект {@link LoanStatementRequestDto}
   * @return добавленный клиент {@link Client}
   */
  protected Client createClient(LoanStatementRequestDto loanStatementRequestDto) {
    Client client = clientMapper.toClient(loanStatementRequestDto);
    log.debug("save client {}", client);
    return clientRepositories.save(client);
  }
}
