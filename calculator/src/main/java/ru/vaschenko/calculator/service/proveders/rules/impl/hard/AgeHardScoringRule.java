package ru.vaschenko.calculator.service.proveders.rules.impl.hard;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.scoring.RejectionAndMessageScoringDTO;
import ru.vaschenko.calculator.service.proveders.rules.ScoringHardRules;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class AgeHardScoringRule implements ScoringHardRules {
  @Value("${scoring.filters.hard.age.min}")
  private Integer minAge;

  @Value("${scoring.filters.hard.age.max}")
  private Integer maxAge;

  /**
   * Выполняет проверку возраста клиента.
   *
   * @param scoringDataDto объект {@link ScoringDataDto}, содержащий дату рождения клиента.
   * @return объект {@link RejectionAndMessageScoringDTO}, содержащий результат проверки
   */
  @Override
  public RejectionAndMessageScoringDTO check(ScoringDataDto scoringDataDto) {
    LocalDate now = LocalDate.now();
    long age = ChronoUnit.YEARS.between(scoringDataDto.getBirthdate(), now);
    return new RejectionAndMessageScoringDTO(
        age >= minAge && age <= maxAge, "Loan denied: Age less than 20 or over 65 years");
  }
}
