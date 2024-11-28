package ru.vaschenko.calculator.service.proveders.rules.impl.soft;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.vaschenko.calculator.dto.scoring.RateAndOtherScoringDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.service.proveders.rules.PreScoringRules;
import ru.vaschenko.calculator.service.proveders.rules.ScoringSoftRules;

import java.math.BigDecimal;

@Component
public class InsuranceSoftPreScoringRule implements ScoringSoftRules, PreScoringRules {
  @Value("${insurance.price}")
  private BigDecimal costInsurance;

  @Value("${scoring.filters.soft.insurance.change_rate}")
  private BigDecimal changeRateValue;

  /**
   * Рассчитывает изменение ставки и стоимость услуги страхования.
   *
   * @param scoringDataDto объект {@link ScoringDataDto}, содержащий информацию о включении услуги страхования.
   * @return {@link RateAndOtherScoringDto} с изменением ставки и стоимости услуги.
   */
  @Override
  public RateAndOtherScoringDto check(ScoringDataDto scoringDataDto) {
    if (scoringDataDto.getIsInsuranceEnabled()) {
      return new RateAndOtherScoringDto(changeRateValue, costInsurance);
    }
    return new RateAndOtherScoringDto(BigDecimal.ZERO, BigDecimal.ZERO);
  }

  @Override
  public RateAndOtherScoringDto check(boolean status) {
    if (status) {
      return new RateAndOtherScoringDto(changeRateValue, costInsurance);
    }
    return new RateAndOtherScoringDto(BigDecimal.ZERO, BigDecimal.ZERO);
  }
}
