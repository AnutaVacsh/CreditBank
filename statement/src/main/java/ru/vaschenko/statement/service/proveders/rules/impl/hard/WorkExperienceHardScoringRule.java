package ru.vaschenko.statement.service.proveders.rules.impl.hard;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vaschenko.statement.dto.ScoringDataDto;
import ru.vaschenko.statement.dto.scoring.RejectionAndMessageScoringDTO;
import ru.vaschenko.statement.service.proveders.rules.ScoringHardRules;

@Service
public class WorkExperienceHardScoringRule implements ScoringHardRules {
  @Value("${scoring.filters.hard.work.experience.total}")
  private Integer workExperienceTotal;

  @Value("${scoring.filters.hard.work.experience.current}")
  private Integer workExperienceCurrent;

  /**
   * Проверяет, соответствует ли общий и текущий стаж работы минимальным требованиям.
   *
   * @param scoringDataDto объект {@link ScoringDataDto}, содержащий данные о стаже работы клиента.
   * @return {@link RejectionAndMessageScoringDTO} с результатом проверки
   */
  @Override
  public RejectionAndMessageScoringDTO check(ScoringDataDto scoringDataDto) {
    return new RejectionAndMessageScoringDTO(
        scoringDataDto.getEmployment().getWorkExperienceTotal() >= workExperienceTotal
            && scoringDataDto.getEmployment().getWorkExperienceCurrent() >= workExperienceCurrent,
        "Loan denied: Total length of service less than 18 months");
  }
}
