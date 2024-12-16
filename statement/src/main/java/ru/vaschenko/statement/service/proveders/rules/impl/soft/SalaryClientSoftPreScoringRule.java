package ru.vaschenko.statement.service.proveders.rules.impl.soft;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vaschenko.statement.dto.ScoringDataDto;
import ru.vaschenko.statement.dto.scoring.RateAndOtherScoringDto;
import ru.vaschenko.statement.service.proveders.rules.PreScoringRules;
import ru.vaschenko.statement.service.proveders.rules.ScoringSoftRules;

@Service
public class SalaryClientSoftPreScoringRule implements ScoringSoftRules, PreScoringRules {
  @Value("${scoring.filters.soft.salary_client.change_rate}")
  private BigDecimal changeRateValue;

  /**
   * Рассчитывает изменение ставки на основе статуса клиента как зарплатного.
   *
   * @param scoringDataDto объект {@link ScoringDataDto}, содержащий информацию о зарплатном статусе клиента.
   * @return {@link RateAndOtherScoringDto} с изменением ставки.
   */
  @Override
  public RateAndOtherScoringDto check(ScoringDataDto scoringDataDto) {
    if (scoringDataDto.getIsSalaryClient()) {
      return new RateAndOtherScoringDto(changeRateValue, BigDecimal.ZERO);
    }
    return new RateAndOtherScoringDto(BigDecimal.ZERO, BigDecimal.ZERO);
  }

  @Override
  public RateAndOtherScoringDto check(boolean status) {
    if (status) {
      return new RateAndOtherScoringDto(changeRateValue, BigDecimal.ZERO);
    }
    return new RateAndOtherScoringDto(BigDecimal.ZERO, BigDecimal.ZERO);
  }
}
