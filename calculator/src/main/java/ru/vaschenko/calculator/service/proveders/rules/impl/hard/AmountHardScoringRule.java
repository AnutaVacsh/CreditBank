package ru.vaschenko.calculator.service.proveders.rules.impl.hard;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.scoring.RejectionAndMessageScoringDTO;
import ru.vaschenko.calculator.service.proveders.rules.ScoringHardRules;

import java.math.BigDecimal;

@Component
public class AmountHardScoringRule implements ScoringHardRules {
  @Value("${scoring.filters.hard.loan_amount.count_salary}")
  private Integer countSalary;

  /**
   * Выполняет проверку что сумма займа меньше, чем 24 зарплат.
   *
   * @param scoringDataDto объект {@link ScoringDataDto}, содержащий дату рождения клиента.
   * @return объект {@link RejectionAndMessageScoringDTO}, содержащий результат проверки
   */
  @Override
  public RejectionAndMessageScoringDTO check(ScoringDataDto scoringDataDto) {
    BigDecimal twentyFiveSalaries =
        scoringDataDto.getEmployment().getSalary().multiply(BigDecimal.valueOf(countSalary));
    return new RejectionAndMessageScoringDTO(
        scoringDataDto.getAmount().compareTo(twentyFiveSalaries) <= 0,
        "Loan denied: The loan amount is more than 24 salaries");
  }
}
