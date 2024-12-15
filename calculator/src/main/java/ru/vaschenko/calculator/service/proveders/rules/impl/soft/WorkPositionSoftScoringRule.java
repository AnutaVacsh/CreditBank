package ru.vaschenko.calculator.service.proveders.rules.impl.soft;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.scoring.RateAndOtherScoringDto;
import ru.vaschenko.calculator.service.proveders.rules.ScoringSoftRules;

@Service
public class WorkPositionSoftScoringRule implements ScoringSoftRules {
  @Value("${scoring.filters.soft.work_position.middle_manager.change_rate}")
  private BigDecimal changeRateValueMiddleManager;

  @Value("${scoring.filters.soft.work_position.top_manager.change_rate}")
  private BigDecimal changeRateValueTopManager;


  /**
   * Рассчитывает изменение ставки на основе должности клиента.
   *
   * @param scoringDataDto объект {@link ScoringDataDto}, содержащий информацию о должности клиента.
   * @return {@link RateAndOtherScoringDto} с изменением ставки.
   */
  @Override
  public RateAndOtherScoringDto check(ScoringDataDto scoringDataDto) {
    switch (scoringDataDto.getEmployment().getPosition()) {
      case MID_MANAGER -> {
        return new RateAndOtherScoringDto(changeRateValueMiddleManager, BigDecimal.ZERO);
      }
      case TOP_MANAGER -> {
        return new RateAndOtherScoringDto(changeRateValueTopManager, BigDecimal.ZERO);
      }
      default -> {
        return new RateAndOtherScoringDto(BigDecimal.ZERO, BigDecimal.ZERO);
      }
    }
  }
}
