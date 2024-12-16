package ru.vaschenko.statement.service.proveders.rules.impl.hard;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vaschenko.statement.dto.ScoringDataDto;
import ru.vaschenko.statement.dto.scoring.RejectionAndMessageScoringDTO;
import ru.vaschenko.statement.service.proveders.rules.ScoringHardRules;

@Service
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
