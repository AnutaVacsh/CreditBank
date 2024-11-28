package ru.vaschenko.calculator.service.proveders.rules.impl.hard;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.vaschenko.calculator.dto.EmploymentDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.scoring.RejectionAndMessageScoringDTO;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class AmountHardScoringRuleTest {

  @Mock private ScoringDataDto scoringDataDto;

  @Mock private EmploymentDto employmentDto;

  @InjectMocks private AmountHardScoringRule amountHardScoringRule;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(amountHardScoringRule, "countSalary", 24);
    when(scoringDataDto.getEmployment()).thenReturn(employmentDto);
    when(employmentDto.getSalary()).thenReturn(BigDecimal.valueOf(5000));
    when(scoringDataDto.getAmount()).thenReturn(BigDecimal.valueOf(100000));
  }

  @Test
  void check_LoanAmountWithinLimit_ShouldReturnTrue() {
    when(scoringDataDto.getEmployment().getSalary()).thenReturn(BigDecimal.valueOf(5000));
    when(scoringDataDto.getAmount()).thenReturn(BigDecimal.valueOf(100000));

    RejectionAndMessageScoringDTO result = amountHardScoringRule.check(scoringDataDto);

    assertTrue(result.rejection());
  }

  @Test
  void check_LoanAmountExceedsLimit_ShouldReturnFalse() {
    when(scoringDataDto.getEmployment().getSalary()).thenReturn(BigDecimal.valueOf(5000));
    when(scoringDataDto.getAmount()).thenReturn(BigDecimal.valueOf(200000));

    RejectionAndMessageScoringDTO result = amountHardScoringRule.check(scoringDataDto);

    assertFalse(result.rejection());
  }
}
