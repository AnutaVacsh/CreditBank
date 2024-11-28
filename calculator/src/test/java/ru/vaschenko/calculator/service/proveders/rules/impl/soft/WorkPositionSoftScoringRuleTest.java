package ru.vaschenko.calculator.service.proveders.rules.impl.soft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.vaschenko.calculator.dto.EmploymentDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.enums.Position;
import ru.vaschenko.calculator.dto.scoring.RateAndOtherScoringDto;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WorkPositionSoftScoringRuleTest {

  WorkPositionSoftScoringRule workPositionSoftScoringRule;

  @BeforeEach
  void setUp() {
    workPositionSoftScoringRule = new WorkPositionSoftScoringRule();
    ReflectionTestUtils.setField(workPositionSoftScoringRule, "changeRateValueMiddleManager", BigDecimal.valueOf(-0.5));
    ReflectionTestUtils.setField(workPositionSoftScoringRule, "changeRateValueTopManager", BigDecimal.valueOf(-1.0));
  }

  @Test
  void check_MiddleManager_ShouldReturnCorrectRateChange() {
    ScoringDataDto scoringDataDto = ScoringDataDto.builder()
            .employment(EmploymentDto.builder()
                    .position(Position.MIDDLE_MANAGER)
                    .build())
            .build();

    RateAndOtherScoringDto result = workPositionSoftScoringRule.check(scoringDataDto);

    assertEquals(BigDecimal.valueOf(-0.5), result.newRate());
    assertEquals(BigDecimal.ZERO, result.otherService());
  }

  @Test
  void check_TopManager_ShouldReturnCorrectRateChange() {
    ScoringDataDto scoringDataDto = ScoringDataDto.builder()
            .employment(EmploymentDto.builder()
                    .position(Position.TOP_MANAGER)
                    .build())
            .build();

    RateAndOtherScoringDto result = workPositionSoftScoringRule.check(scoringDataDto);

    assertEquals(BigDecimal.valueOf(-1.0), result.newRate());
    assertEquals(BigDecimal.ZERO, result.otherService());
  }
}

