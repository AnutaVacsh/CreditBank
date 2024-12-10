package ru.vaschenko.deal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.deal.models.Credit;
import ru.vaschenko.deal.repositories.CreditRepositories;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditService {
  private final CreditRepositories creditRepositories;

  /**
   * Сохранение кредита в бд
   *
   * @param credit {@link Credit}
   * @return сохранённый кредит {@link Credit}
   */
  public Credit safeCredit(Credit credit) {
    log.debug("safe credit {}", credit);
    return creditRepositories.save(credit);
  }
}
