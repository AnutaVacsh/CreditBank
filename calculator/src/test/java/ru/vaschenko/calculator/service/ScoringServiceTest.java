package ru.vaschenko.calculator.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vaschenko.calculator.dto.EmploymentDto;
import ru.vaschenko.calculator.dto.PaymentScheduleElementDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.enums.EmploymentStatus;
import ru.vaschenko.calculator.dto.enums.Gender;
import ru.vaschenko.calculator.dto.enums.MaritalStatus;
import ru.vaschenko.calculator.dto.enums.Position;
import ru.vaschenko.calculator.dto.scoring.RateAndOtherScoringDto;
import ru.vaschenko.calculator.service.proveders.ScoringProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {

  @Mock private ScoringProvider scoringProvider;

  @InjectMocks private ScoringService scoringService;

  @BeforeEach
  void setUp() {
    scoringService.baseRate = BigDecimal.valueOf(15);
    scoringService.insurancePrice = BigDecimal.valueOf(100000);
  }

  @Test
  void calculateRate_withScoringDataDto() {
    // Создаем объект данных для скоринга
    ScoringDataDto scoringData = ScoringDataDto.builder()
            .employment(
                    EmploymentDto.builder()
                            .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                            .employerINN("7712345678")
                            .salary(new BigDecimal("50000.00"))
                            .position(Position.MIDDLE_MANAGER)
                            .workExperienceTotal(24)
                            .workExperienceCurrent(12)
                            .build())
            .amount(new BigDecimal("100000"))
            .maritalStatus(MaritalStatus.MARRIED)
            .birthdate(LocalDate.of(1990, 5, 15)) // Пример даты рождения
            .gender(Gender.MALE)
            .isInsuranceEnabled(true)
            .isSalaryClient(true)
            .build();

    RateAndOtherScoringDto mockedScoringDto = new RateAndOtherScoringDto(new BigDecimal("5.00"), BigDecimal.ZERO);
    when(scoringProvider.fullScoring(scoringData)).thenReturn(mockedScoringDto);

    BigDecimal result = scoringService.calculateRate(scoringData);

    verify(scoringProvider, times(1)).fullScoring(scoringData);

    assertEquals(new BigDecimal("5.00"), result);
  }

  @Test
  void calculateRate_withInsuranceAndSalaryClient() {
    assertEquals(new BigDecimal(11), scoringService.calculateRate(true, true));
    assertEquals(new BigDecimal(12), scoringService.calculateRate(true, false));
    assertEquals(new BigDecimal(14), scoringService.calculateRate(false, true));
    assertEquals(new BigDecimal(15), scoringService.calculateRate(false, false));
  }

  @Test
  void calculateTotalAmount() {
    assertEquals(
        new BigDecimal(1100000),
        scoringService.calculateTotalAmount(new BigDecimal(1000000), true));
    assertEquals(
        new BigDecimal(1000000),
        scoringService.calculateTotalAmount(new BigDecimal(1000000), false));
  }

  @Test
  void calculateMonthlyPayment() {
    assertEquals(
        new BigDecimal("48486.65"),
        scoringService.calculateMonthlyPayment(new BigDecimal(1000000), new BigDecimal(15), 24));
    assertEquals(
        new BigDecimal("51268.62"),
        scoringService.calculateMonthlyPayment(new BigDecimal(1100000), new BigDecimal(11), 24));
  }

  @Test
  void calculatePaymentSchedule() {
    List<PaymentScheduleElementDto> listElement =
        scoringService.calculatePaymentSchedule(
            new BigDecimal(1000000), new BigDecimal(14), 24, new BigDecimal("48012.88"));

    assertEquals(24, listElement.size());
    assertEquals(new BigDecimal("736496.51"), listElement.get(6).getRemainingDebt());
    assertEquals(new BigDecimal("40816.29"), listElement.get(10).getDebtPayment());
  }

  @Test
  void calculatePsk() {
    assertEquals(
        new BigDecimal("16.37"),
        scoringService
            .calculatePsk(new BigDecimal(1000000), new BigDecimal("48486.65"), 24)
            .setScale(2, RoundingMode.HALF_UP));
  }
}
