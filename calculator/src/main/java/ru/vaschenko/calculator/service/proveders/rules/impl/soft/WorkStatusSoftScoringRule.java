package ru.vaschenko.calculator.service.proveders.rules.impl.soft;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.vaschenko.calculator.dto.scoring.RateAndOtherScoringDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.service.proveders.rules.ScoringSoftRules;

import java.math.BigDecimal;

@Component
public class WorkStatusSoftScoringRule implements ScoringSoftRules {
  @Value("${scoring.filters.soft.work_status.self_employed.change_rate}")
  private BigDecimal changeRateValueSelfEmployed;

  @Value("${scoring.filters.soft.work_status.businessman.change_rate}")
  private BigDecimal changeRateValueBusinessman;

  /**
   * Рассчитывает изменение ставки на основе позиции на работе.
   *
   * @param scoringDataDto объект {@link ScoringDataDto}, содержащий информацию о позиции на работе клиента.
   * @return {@link RateAndOtherScoringDto} с изменением ставки.
   */
  @Override
  public RateAndOtherScoringDto check(ScoringDataDto scoringDataDto) {
    switch (scoringDataDto.getEmployment().getEmploymentStatus()) {
      case SELF_EMPLOYED -> {
        return new RateAndOtherScoringDto(changeRateValueSelfEmployed, BigDecimal.ZERO);
      }
      case BUSINESS_OWNER -> {
        return new RateAndOtherScoringDto(changeRateValueBusinessman, BigDecimal.ZERO);
      }
      default -> {
        return new RateAndOtherScoringDto(BigDecimal.ZERO, BigDecimal.ZERO);
      }
    }
  }
}
