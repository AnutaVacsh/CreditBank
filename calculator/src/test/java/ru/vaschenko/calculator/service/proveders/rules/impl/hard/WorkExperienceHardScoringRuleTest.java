package ru.vaschenko.calculator.service.proveders.rules.impl.hard;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.vaschenko.calculator.dto.EmploymentDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.scoring.RejectionAndMessageScoringDTO;

@ExtendWith(MockitoExtension.class)
class WorkExperienceHardScoringRuleTest {

  @Mock private ScoringDataDto scoringDataDto;

  @Mock private EmploymentDto employmentDto;

  private WorkExperienceHardScoringRule workExperienceHardScoringRule;

  @BeforeEach
  void setUp() {
    workExperienceHardScoringRule = new WorkExperienceHardScoringRule();

    ReflectionTestUtils.setField(workExperienceHardScoringRule, "workExperienceTotal", 18);
    ReflectionTestUtils.setField(workExperienceHardScoringRule, "workExperienceCurrent", 6);

    when(scoringDataDto.getEmployment()).thenReturn(employmentDto);
  }

  @Test
  void check_WorkExperienceBelowTotalRequirement_ShouldReturnFalse() {
    RejectionAndMessageScoringDTO result = workExperienceHardScoringRule.check(scoringDataDto);

    assertFalse(result.rejection());
  }

  @Test
  void check_WorkExperienceBelowCurrentRequirement_ShouldReturnFalse() {
    RejectionAndMessageScoringDTO result = workExperienceHardScoringRule.check(scoringDataDto);

    assertFalse(result.rejection());
  }
}
