package ru.vaschenko.calculator.service.proveders.rules.impl.hard;

import org.springframework.stereotype.Service;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.enums.EmploymentStatus;
import ru.vaschenko.calculator.dto.scoring.RejectionAndMessageScoringDTO;
import ru.vaschenko.calculator.service.proveders.rules.ScoringHardRules;

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
