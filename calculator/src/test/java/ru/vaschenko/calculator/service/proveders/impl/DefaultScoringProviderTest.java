package ru.vaschenko.calculator.service.proveders.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.scoring.PreScoringInfoDTO;
import ru.vaschenko.calculator.dto.scoring.RateAndOtherScoringDto;
import ru.vaschenko.calculator.dto.scoring.RejectionAndMessageScoringDTO;
import ru.vaschenko.calculator.exception.ScoringCalculationException;
import ru.vaschenko.calculator.service.proveders.rules.ScoringHardRules;
import ru.vaschenko.calculator.service.proveders.rules.PreScoringRules;
import ru.vaschenko.calculator.service.proveders.rules.ScoringSoftRules;

@ExtendWith(MockitoExtension.class)
class DefaultScoringProviderTest {

  @Mock private ScoringHardRules hardRule1;
  @Mock private ScoringHardRules hardRule2;
  @Mock private ScoringSoftRules softRule1;
  @Mock private ScoringSoftRules softRule2;
  @Mock private PreScoringRules loanRule1;
  @Mock private PreScoringRules loanRule2;

  @InjectMocks private DefaultScoringProvider defaultScoringProvider;

  @BeforeEach
  void setUp() {
    defaultScoringProvider =
        new DefaultScoringProvider(
            List.of(hardRule1, hardRule2),
            List.of(softRule1, softRule2),
            List.of(loanRule1, loanRule2));
    ReflectionTestUtils.setField(defaultScoringProvider, "rate", BigDecimal.valueOf(10.0));
  }

  @Test
  void fullScoring_WhenHardFiltersFail_ShouldThrowException() {
    ScoringDataDto scoringDataDto = mock(ScoringDataDto.class);
    when(hardRule1.check(scoringDataDto))
        .thenReturn(new RejectionAndMessageScoringDTO(false, "Rule 1 failed"));
    when(hardRule2.check(scoringDataDto)).thenReturn(new RejectionAndMessageScoringDTO(true, ""));

    ScoringCalculationException exception =
        assertThrows(
            ScoringCalculationException.class,
            () -> defaultScoringProvider.fullScoring(scoringDataDto));
    assertEquals("Rule 1 failed", exception.getMessage());
  }

  @Test
  void fullScoring_WhenHardFiltersPass_ShouldReturnCorrectSoftScoring() {
    ScoringDataDto scoringDataDto = mock(ScoringDataDto.class);
    when(hardRule1.check(scoringDataDto)).thenReturn(new RejectionAndMessageScoringDTO(true, ""));
    when(hardRule2.check(scoringDataDto)).thenReturn(new RejectionAndMessageScoringDTO(true, ""));
    when(softRule1.check(scoringDataDto))
        .thenReturn(new RateAndOtherScoringDto(BigDecimal.valueOf(-1), BigDecimal.ZERO));
    when(softRule2.check(scoringDataDto))
        .thenReturn(new RateAndOtherScoringDto(BigDecimal.valueOf(-0.5), BigDecimal.ZERO));

    RateAndOtherScoringDto result = defaultScoringProvider.fullScoring(scoringDataDto);

    assertEquals(BigDecimal.valueOf(8.5), result.newRate());
    assertEquals(BigDecimal.ZERO, result.otherService());
  }

  @Test
  void preScoring_ShouldReturnCorrectResults() {
    // Мокирование данных
    when(loanRule1.check(true))
        .thenReturn(new RateAndOtherScoringDto(BigDecimal.valueOf(-0.5), BigDecimal.ZERO));
    when(loanRule1.check(false))
        .thenReturn(new RateAndOtherScoringDto(BigDecimal.ZERO, BigDecimal.ZERO));
    when(loanRule2.check(true))
        .thenReturn(new RateAndOtherScoringDto(BigDecimal.valueOf(-1.0), BigDecimal.ZERO));
    when(loanRule2.check(false))
        .thenReturn(new RateAndOtherScoringDto(BigDecimal.ZERO, BigDecimal.ZERO));

    List<PreScoringInfoDTO> preScoringResults = defaultScoringProvider.preScoring();

    assertEquals(4, preScoringResults.size());

    PreScoringInfoDTO result1 = preScoringResults.get(0);
    assertEquals(BigDecimal.valueOf(10.0), result1.rateAndOtherScoringDto().newRate());
    assertEquals(BigDecimal.ZERO, result1.rateAndOtherScoringDto().otherService());
  }

  @Test
  void hardScoring_WhenErrorMessagesExist_ShouldReturnErrorMessage() {
    // Мокирование данных
    ScoringDataDto scoringDataDto = mock(ScoringDataDto.class);
    when(hardRule1.check(scoringDataDto))
        .thenReturn(new RejectionAndMessageScoringDTO(false, "Hard rule 1 failed"));
    when(hardRule2.check(scoringDataDto)).thenReturn(new RejectionAndMessageScoringDTO(true, ""));

    RejectionAndMessageScoringDTO result = defaultScoringProvider.hardScoring(scoringDataDto);
    assertFalse(result.rejection());
    assertEquals("Hard rule 1 failed", result.message());
  }

  @Test
  void softScoring_ShouldReturnCorrectRate() {
    ScoringDataDto scoringDataDto = mock(ScoringDataDto.class);
    when(softRule1.check(scoringDataDto))
        .thenReturn(new RateAndOtherScoringDto(BigDecimal.valueOf(-1), BigDecimal.ZERO));
    when(softRule2.check(scoringDataDto))
        .thenReturn(new RateAndOtherScoringDto(BigDecimal.valueOf(-0.5), BigDecimal.ZERO));

    RateAndOtherScoringDto result =
        defaultScoringProvider.softScoring(scoringDataDto, BigDecimal.valueOf(10.0));
    assertEquals(BigDecimal.valueOf(8.5), result.newRate());
    assertEquals(BigDecimal.ZERO, result.otherService());
  }
}
