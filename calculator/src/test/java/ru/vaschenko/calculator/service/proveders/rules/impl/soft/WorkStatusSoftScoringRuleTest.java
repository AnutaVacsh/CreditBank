package ru.vaschenko.calculator.service.proveders.rules.impl.soft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.vaschenko.calculator.dto.EmploymentDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.enums.EmploymentStatus;
import ru.vaschenko.calculator.dto.scoring.RateAndOtherScoringDto;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WorkStatusSoftScoringRuleTest {

  WorkStatusSoftScoringRule workStatusSoftScoringRule;

  @BeforeEach
  void setUp() {
    workStatusSoftScoringRule = new WorkStatusSoftScoringRule();
    ReflectionTestUtils.setField(workStatusSoftScoringRule, "changeRateValueSelfEmployed", BigDecimal.valueOf(-0.3));
    ReflectionTestUtils.setField(workStatusSoftScoringRule, "changeRateValueBusinessman", BigDecimal.valueOf(-0.7));
  }

  @Test
  void check_SelfEmployed_ShouldReturnCorrectRateChange() {
    ScoringDataDto scoringDataDto = ScoringDataDto.builder()
            .employment(EmploymentDto.builder()
                    .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                    .build())
            .build();

    RateAndOtherScoringDto result = workStatusSoftScoringRule.check(scoringDataDto);

    assertEquals(BigDecimal.valueOf(-0.3), result.newRate());
    assertEquals(BigDecimal.ZERO, result.otherService());
  }

  @Test
  void check_BusinessOwner_ShouldReturnCorrectRateChange() {
    ScoringDataDto scoringDataDto = ScoringDataDto.builder()
            .employment(EmploymentDto.builder()
                    .employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                    .build())
            .build();

    RateAndOtherScoringDto result = workStatusSoftScoringRule.check(scoringDataDto);

    assertEquals(BigDecimal.valueOf(-0.7), result.newRate());
    assertEquals(BigDecimal.ZERO, result.otherService());
  }

  @Test
  void check_OtherEmploymentStatus_ShouldReturnZeroRateChange() {
    ScoringDataDto scoringDataDto = ScoringDataDto.builder()
            .employment(EmploymentDto.builder()
                    .employmentStatus(EmploymentStatus.EMPLOYED)
                    .build())
            .build();

    RateAndOtherScoringDto result = workStatusSoftScoringRule.check(scoringDataDto);

    assertEquals(BigDecimal.ZERO, result.newRate());
    assertEquals(BigDecimal.ZERO, result.otherService());
  }
}

