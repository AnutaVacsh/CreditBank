package ru.vaschenko.statement.service.proveders.rules.impl.soft;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vaschenko.statement.dto.ScoringDataDto;
import ru.vaschenko.statement.dto.enums.Gender;
import ru.vaschenko.statement.dto.scoring.RateAndOtherScoringDto;
import ru.vaschenko.statement.service.proveders.rules.ScoringSoftRules;

@Service
public class GenderAgeSoftScoringRule implements ScoringSoftRules {
    @Value("${scoring.filters.soft.gender_age.age_female.min}")
    private Integer minAgeFemale;
    @Value("${scoring.filters.soft.gender_age.age_female.max}")
    private Integer maxAgeFemale;
    @Value("${scoring.filters.soft.gender_age.age_male.min}")
    private Integer minAgeMale;
    @Value("${scoring.filters.soft.gender_age.age_male.max}")
    private Integer maxAgeMale;

    @Value("${scoring.filters.soft.gender_age.age_female.change_rate}")
    private BigDecimal changeRateFemaleValue;
    @Value("${scoring.filters.soft.gender_age.not_binary.change_rate}")
    private BigDecimal changeRateNotBinaryValue;

    /**
     * Рассчитывает изменение ставки в зависимости от пола и возраста клиента.
     *
     * @param scoringDataDto объект {@link ScoringDataDto}, содержащий данные о поле и дате рождения клиента.
     * @return {@link RateAndOtherScoringDto} с изменением ставки.
     */
    @Override
    public RateAndOtherScoringDto check(ScoringDataDto scoringDataDto) {
        Gender gender = scoringDataDto.getGender();
        long age = ChronoUnit.YEARS.between(scoringDataDto.getBirthdate(), LocalDate.now());
        if(gender==Gender.FEMALE && (age>=minAgeFemale && age<=maxAgeFemale)
                || gender==Gender.MALE && (age>=minAgeMale && age<=maxAgeMale))
        {
            return new RateAndOtherScoringDto(changeRateFemaleValue, BigDecimal.ZERO);
        } else if (gender == Gender.NON_BINARY) {
            return new RateAndOtherScoringDto(changeRateNotBinaryValue, BigDecimal.ZERO);
        }
        return new RateAndOtherScoringDto(BigDecimal.ZERO, BigDecimal.ZERO);
    }
}
