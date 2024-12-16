package ru.vaschenko.statement.service.proveders.rules.impl.soft;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vaschenko.statement.dto.ScoringDataDto;
import ru.vaschenko.statement.dto.scoring.RateAndOtherScoringDto;
import ru.vaschenko.statement.service.proveders.rules.ScoringSoftRules;

@Service
public class MaritalStatusSoftScoringRule implements ScoringSoftRules {
    @Value("${scoring.filters.soft.marital_status.single.change_rate}")
    private BigDecimal changeRateValueSingleStatus;
    @Value("${scoring.filters.soft.marital_status.married.change_rate}")
    private BigDecimal changeRateValueMarriedStatus;

    /**
     * Рассчитывает изменение ставки на основе семейного положения.
     *
     * @param scoringDataDto объект {@link ScoringDataDto}, содержащий информацию о семейном положении.
     * @return {@link RateAndOtherScoringDto} с изменением ставки.
     */
    @Override
    public RateAndOtherScoringDto check(ScoringDataDto scoringDataDto) {
        switch(scoringDataDto.getMaritalStatus()) {
            case SINGLE -> {
                return new RateAndOtherScoringDto(changeRateValueSingleStatus, BigDecimal.ZERO);
            }
            case MARRIED -> {
                return new RateAndOtherScoringDto(changeRateValueMarriedStatus, BigDecimal.ZERO);
            }
            default -> {
                return new RateAndOtherScoringDto(BigDecimal.ZERO, BigDecimal.ZERO);
            }
        }
    }
}
