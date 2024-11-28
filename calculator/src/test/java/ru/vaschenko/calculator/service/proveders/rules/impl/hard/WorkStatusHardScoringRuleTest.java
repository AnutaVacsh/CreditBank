package ru.vaschenko.calculator.service.proveders.rules.impl.hard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vaschenko.calculator.dto.EmploymentDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.enums.EmploymentStatus;
import ru.vaschenko.calculator.dto.scoring.RejectionAndMessageScoringDTO;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WorkStatusHardScoringRuleTest {

  @InjectMocks private WorkStatusHardScoringRule workStatusHardScoringRule;

  private ScoringDataDto scoringDataDto;

  @BeforeEach
  void setUp() {
    EmploymentDto employmentDto =
        EmploymentDto.builder().employmentStatus(EmploymentStatus.UNEMPLOYED).build();
    scoringDataDto = ScoringDataDto.builder().employment(employmentDto).build();
  }

  @Test
  void check_Unemployed_ShouldReturnFalse() {
    RejectionAndMessageScoringDTO result = workStatusHardScoringRule.check(scoringDataDto);
    assertFalse(result.rejection());
  }

  @Test
  void check_Employed_ShouldReturnTrue() {
    scoringDataDto.getEmployment().setEmploymentStatus(EmploymentStatus.EMPLOYED);

    RejectionAndMessageScoringDTO result = workStatusHardScoringRule.check(scoringDataDto);
    assertTrue(result.rejection());
  }
}
