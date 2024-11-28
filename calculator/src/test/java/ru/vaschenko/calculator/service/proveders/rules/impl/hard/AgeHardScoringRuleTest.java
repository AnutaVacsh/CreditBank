package ru.vaschenko.calculator.service.proveders.rules.impl.hard;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.scoring.RejectionAndMessageScoringDTO;

@ExtendWith(MockitoExtension.class)
class AgeHardScoringRuleTest {

  AgeHardScoringRule ageHardScoringRule;

  @BeforeEach
  void setUp() {
    ageHardScoringRule = new AgeHardScoringRule();
    ReflectionTestUtils.setField(ageHardScoringRule, "minAge", 20);
    ReflectionTestUtils.setField(ageHardScoringRule, "maxAge", 65);
  }

  @Test
  void check_AgeWithinRange_ShouldReturnTrue() {
    ScoringDataDto scoringDataDto = ScoringDataDto.builder().build();
    scoringDataDto.setBirthdate(LocalDate.now().minusYears(30));

    RejectionAndMessageScoringDTO result = ageHardScoringRule.check(scoringDataDto);

    assertTrue(result.rejection());
  }

  @Test
  void check_AgeBelowMin_ShouldReturnFalse() {
    ScoringDataDto scoringDataDto = ScoringDataDto.builder().build();
    scoringDataDto.setBirthdate(LocalDate.now().minusYears(18));

    RejectionAndMessageScoringDTO result = ageHardScoringRule.check(scoringDataDto);

    assertFalse(result.rejection());
  }

  @Test
  void check_AgeAboveMax_ShouldReturnFalse() {
    ScoringDataDto scoringDataDto = ScoringDataDto.builder().build();
    scoringDataDto.setBirthdate(LocalDate.now().minusYears(70));

    RejectionAndMessageScoringDTO result = ageHardScoringRule.check(scoringDataDto);

    assertFalse(result.rejection());
  }
}
