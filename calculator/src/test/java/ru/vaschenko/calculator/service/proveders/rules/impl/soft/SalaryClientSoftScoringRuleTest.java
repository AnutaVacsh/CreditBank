package ru.vaschenko.calculator.service.proveders.rules.impl.soft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.scoring.RateAndOtherScoringDto;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class SalaryClientSoftScoringRuleTest {

    SalaryClientSoftPreScoringRule salaryClientSoftScoringRule;

    @BeforeEach
    void setUp() {
        salaryClientSoftScoringRule = new SalaryClientSoftPreScoringRule();
        ReflectionTestUtils.setField(salaryClientSoftScoringRule, "changeRateValue", BigDecimal.valueOf(-1.5));
    }

    @Test
    void check_SalaryClient_ShouldReturnRateChange() {
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .isSalaryClient(true)
                .build();

        RateAndOtherScoringDto result = salaryClientSoftScoringRule.check(scoringDataDto);

        assertEquals(BigDecimal.valueOf(-1.5), result.newRate());
        assertEquals(BigDecimal.ZERO, result.otherService());
    }

    @Test
    void check_NotSalaryClient_ShouldReturnZeroRateChange() {
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .isSalaryClient(false)
                .build();

        RateAndOtherScoringDto result = salaryClientSoftScoringRule.check(scoringDataDto);

        assertEquals(BigDecimal.ZERO, result.newRate());
        assertEquals(BigDecimal.ZERO, result.otherService());
    }

    @Test
    void check_WithTrueStatus_ShouldReturnRateChange() {
        RateAndOtherScoringDto result = salaryClientSoftScoringRule.check(true);

        assertEquals(BigDecimal.valueOf(-1.5), result.newRate());
        assertEquals(BigDecimal.ZERO, result.otherService());
    }

    @Test
    void check_WithFalseStatus_ShouldReturnZeroRateChange() {
        RateAndOtherScoringDto result = salaryClientSoftScoringRule.check(false);

        assertEquals(BigDecimal.ZERO, result.newRate());
        assertEquals(BigDecimal.ZERO, result.otherService());
    }
}
