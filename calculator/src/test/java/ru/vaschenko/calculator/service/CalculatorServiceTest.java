package ru.vaschenko.calculator.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vaschenko.calculator.dto.CreditDto;
import ru.vaschenko.calculator.dto.EmploymentDto;
import ru.vaschenko.calculator.dto.PaymentScheduleElementDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.enums.EmploymentStatus;
import ru.vaschenko.calculator.dto.enums.Gender;
import ru.vaschenko.calculator.dto.enums.MaritalStatus;
import ru.vaschenko.calculator.dto.enums.Position;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculatorServiceTest {

  @Mock private ScoringService scoringService;

  @InjectMocks CalculatorService calculatorService;

  @Test
  void calculateCredit() {
    ScoringDataDto scoringData =
        ScoringDataDto.builder()
            .employment(
                EmploymentDto.builder()
                    .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                    .employerINN("7712345678")
                    .salary(new BigDecimal(50000))
                    .position(Position.MID_MANAGER)
                    .workExperienceTotal(24)
                    .workExperienceCurrent(12)
                    .build())
            .amount(new BigDecimal(1000000))
            .term(24)
            .maritalStatus(MaritalStatus.MARRIED)
            .birthdate(LocalDate.of(1990, 5, 15))
            .gender(Gender.MALE)
            .isInsuranceEnabled(true)
            .isSalaryClient(false)
            .build();

    when(scoringService.calculateRate(scoringData)).thenReturn(BigDecimal.valueOf(12));

    when(scoringService.calculateTotalAmount(eq(new BigDecimal(1000000)), eq(true)))
        .thenReturn(new BigDecimal(1100000));

    when(scoringService.calculateMonthlyPayment(
            new BigDecimal(1100000), new BigDecimal(12),(24)))
        .thenReturn(BigDecimal.valueOf(51781));

    when(scoringService.calculatePsk(
            new BigDecimal(1100000), new BigDecimal(51781), 24))
        .thenReturn(new BigDecimal("12.98").setScale(2, RoundingMode.HALF_UP));

    when(scoringService.calculatePaymentSchedule(
            new BigDecimal(1100000), new BigDecimal(12), 24, new BigDecimal(51781)))
        .thenAnswer(
            invocation ->
                new ArrayList<PaymentScheduleElementDto>(
                    Collections.nCopies(
                        invocation.getArgument(2), PaymentScheduleElementDto.builder().build())));

    CreditDto credit = calculatorService.calculateCredit(scoringData);

    assertNotNull(credit);
    assertEquals(new BigDecimal(1100000), credit.getAmount());
    assertEquals(24, credit.getTerm());
    assertEquals(new BigDecimal(51781), credit.getMonthlyPayment());
    assertEquals(new BigDecimal(12), credit.getRate());

    assertEquals(new BigDecimal("12.98").setScale(2, RoundingMode.HALF_UP), credit.getPsk());
    assertTrue(credit.getIsInsuranceEnabled());
    assertFalse(credit.getIsSalaryClient());
    assertNotNull(credit.getPaymentSchedule());
    assertEquals(credit.getTerm(), credit.getPaymentSchedule().size());

    verify(scoringService).calculateRate(scoringData);
    verify(scoringService).calculateTotalAmount(new BigDecimal(1000000), true);
    verify(scoringService).calculateMonthlyPayment(new BigDecimal(1100000), new BigDecimal(12), 24);
    verify(scoringService).calculatePsk(new BigDecimal(1100000), new BigDecimal(51781), 24);
    verify(scoringService)
        .calculatePaymentSchedule(
            new BigDecimal(1100000), new BigDecimal(12), 24, new BigDecimal(51781));
  }
}
