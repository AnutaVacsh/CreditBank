package ru.vaschenko.calculator.service.proveders.rules.impl.soft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.scoring.RateAndOtherScoringDto;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class InsuranceSoftScoringRuleTest {

    @InjectMocks
    private InsuranceSoftPreScoringRule insuranceSoftScoringRule;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(insuranceSoftScoringRule, "costInsurance", BigDecimal.valueOf(5000));
        ReflectionTestUtils.setField(insuranceSoftScoringRule, "changeRateValue", BigDecimal.valueOf(-1.5));
    }

    @Test
    void check_InsuranceEnabled_ShouldReturnReducedRateAndCost() {
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .isInsuranceEnabled(true)
                .build();

        RateAndOtherScoringDto result = insuranceSoftScoringRule.check(scoringDataDto);

        assertEquals(BigDecimal.valueOf(-1.5), result.newRate());
        assertEquals(BigDecimal.valueOf(5000), result.otherService());
    }

    @Test
    void check_InsuranceDisabled_ShouldReturnZeroRateAndCost() {
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .isInsuranceEnabled(false)
                .build();

        RateAndOtherScoringDto result = insuranceSoftScoringRule.check(scoringDataDto);

        assertEquals(BigDecimal.ZERO, result.newRate());
        assertEquals(BigDecimal.ZERO, result.otherService());
    }

    @Test
    void check_WithTrueStatus_ShouldReturnReducedRateAndCost() {
        RateAndOtherScoringDto result = insuranceSoftScoringRule.check(true);

        assertEquals(BigDecimal.valueOf(-1.5), result.newRate());
        assertEquals(BigDecimal.valueOf(5000), result.otherService());
    }

    @Test
    void check_WithFalseStatus_ShouldReturnZeroRateAndCost() {
        RateAndOtherScoringDto result = insuranceSoftScoringRule.check(false);

        assertEquals(BigDecimal.ZERO, result.newRate());
        assertEquals(BigDecimal.ZERO, result.otherService());
    }
}