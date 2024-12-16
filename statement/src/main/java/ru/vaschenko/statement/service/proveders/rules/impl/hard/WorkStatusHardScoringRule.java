package ru.vaschenko.statement.service.proveders.rules.impl.hard;

import org.springframework.stereotype.Service;
import ru.vaschenko.statement.dto.ScoringDataDto;
import ru.vaschenko.statement.dto.enums.EmploymentStatus;
import ru.vaschenko.statement.dto.scoring.RejectionAndMessageScoringDTO;
import ru.vaschenko.statement.service.proveders.rules.ScoringHardRules;

@Service
public class WorkStatusHardScoringRule implements ScoringHardRules {

  /**
   * Проверяет, является ли клиент безработным.
   *
   * @param scoringDataDto объект {@link ScoringDataDto}, содержащий информацию о статусе занятости клиента.
   * @return {@link RejectionAndMessageScoringDTO} с результатом проверки
   */
  @Override
  public RejectionAndMessageScoringDTO check(ScoringDataDto scoringDataDto) {
    return new RejectionAndMessageScoringDTO(
        scoringDataDto.getEmployment().getEmploymentStatus() != EmploymentStatus.UNEMPLOYED,
        "Loan denied: Unemployed");
  }
}
