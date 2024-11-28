package ru.vaschenko.calculator.service.proveders.rules.impl.soft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.enums.MaritalStatus;
import ru.vaschenko.calculator.dto.scoring.RateAndOtherScoringDto;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class MaritalStatusSoftScoringRuleTest {

  MaritalStatusSoftScoringRule maritalStatusSoftScoringRule;

  @BeforeEach
  void setUp() {
    maritalStatusSoftScoringRule = new MaritalStatusSoftScoringRule();
    ReflectionTestUtils.setField(maritalStatusSoftScoringRule, "changeRateValueSingleStatus", BigDecimal.valueOf(-1.5));
    ReflectionTestUtils.setField(maritalStatusSoftScoringRule, "changeRateValueMarriedStatus", BigDecimal.valueOf(-0.5));
  }

  @Test
  void check_SingleStatus_ShouldReturnCorrectRateChange() {
    ScoringDataDto scoringDataDto = ScoringDataDto.builder()
            .maritalStatus(MaritalStatus.SINGLE)
            .build();

    RateAndOtherScoringDto result = maritalStatusSoftScoringRule.check(scoringDataDto);

    assertEquals(BigDecimal.valueOf(-1.5), result.newRate());
    assertEquals(BigDecimal.ZERO, result.otherService());
  }

  @Test
  void check_MarriedStatus_ShouldReturnCorrectRateChange() {
    ScoringDataDto scoringDataDto = ScoringDataDto.builder()
            .maritalStatus(MaritalStatus.MARRIED)
            .build();

    RateAndOtherScoringDto result = maritalStatusSoftScoringRule.check(scoringDataDto);

    assertEquals(BigDecimal.valueOf(-0.5), result.newRate());
    assertEquals(BigDecimal.ZERO, result.otherService());
  }

  @Test
  void check_OtherStatus_ShouldReturnZeroRateChange() {
    ScoringDataDto scoringDataDto = ScoringDataDto.builder()
            .maritalStatus(MaritalStatus.DIVORCED)
            .build();

    RateAndOtherScoringDto result = maritalStatusSoftScoringRule.check(scoringDataDto);

    assertEquals(BigDecimal.ZERO, result.newRate());
    assertEquals(BigDecimal.ZERO, result.otherService());
  }
}

