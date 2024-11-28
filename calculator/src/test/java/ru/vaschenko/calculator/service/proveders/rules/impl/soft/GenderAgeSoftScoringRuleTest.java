package ru.vaschenko.calculator.service.proveders.rules.impl.soft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.enums.Gender;
import ru.vaschenko.calculator.dto.scoring.RateAndOtherScoringDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GenderAgeSoftScoringRuleTest {

  @InjectMocks private GenderAgeSoftScoringRule genderAgeSoftScoringRule;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(genderAgeSoftScoringRule, "minAgeFemale", 20);
    ReflectionTestUtils.setField(genderAgeSoftScoringRule, "maxAgeFemale", 60);
    ReflectionTestUtils.setField(genderAgeSoftScoringRule, "minAgeMale", 25);
    ReflectionTestUtils.setField(genderAgeSoftScoringRule, "maxAgeMale", 65);
    ReflectionTestUtils.setField(
        genderAgeSoftScoringRule, "changeRateFemaleValue", BigDecimal.valueOf(-0.5));
    ReflectionTestUtils.setField(
        genderAgeSoftScoringRule, "changeRateNotBinaryValue", BigDecimal.valueOf(-1.0));
  }

  @Test
  void check_FemaleWithinAgeRange_ShouldReturnReducedRate() {
    ScoringDataDto scoringDataDto =
        ScoringDataDto.builder()
            .gender(Gender.FEMALE)
            .birthdate(LocalDate.now().minusYears(30))
            .build();

    RateAndOtherScoringDto result = genderAgeSoftScoringRule.check(scoringDataDto);

    assertEquals(BigDecimal.valueOf(-0.5), result.newRate());
  }

  @Test
  void check_MaleWithinAgeRange_ShouldReturnReducedRate() {
    ScoringDataDto scoringDataDto =
        ScoringDataDto.builder()
            .gender(Gender.MALE)
            .birthdate(LocalDate.now().minusYears(40))
            .build();

    RateAndOtherScoringDto result = genderAgeSoftScoringRule.check(scoringDataDto);

    assertEquals(BigDecimal.valueOf(-0.5), result.newRate());
  }

  @Test
  void check_NonBinary_ShouldReturnDifferentReducedRate() {
    ScoringDataDto scoringDataDto =
        ScoringDataDto.builder()
            .gender(Gender.NON_BINARY)
            .birthdate(LocalDate.now().minusYears(30))
            .build();

    RateAndOtherScoringDto result = genderAgeSoftScoringRule.check(scoringDataDto);

    assertEquals(BigDecimal.valueOf(-1.0), result.newRate());
  }

  @Test
  void check_OutOfAgeRange_ShouldReturnZeroRateChange() {
    ScoringDataDto scoringDataDto =
        ScoringDataDto.builder()
            .gender(Gender.FEMALE)
            .birthdate(LocalDate.now().minusYears(70))
            .build();

    RateAndOtherScoringDto result = genderAgeSoftScoringRule.check(scoringDataDto);

    assertEquals(BigDecimal.ZERO, result.newRate());
  }
}
