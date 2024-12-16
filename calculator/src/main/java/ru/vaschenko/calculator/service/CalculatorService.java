package ru.vaschenko.calculator.service;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.calculator.dto.CreditDto;
import ru.vaschenko.calculator.dto.PaymentScheduleElementDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculatorService {
  private final ScoringService scoringService;

  /**
   * Выполняет расчет условий кредита.
   *
   * @param scoringData данные для расчета условий кредита, включая сумму, срок, и дополнительные
   *     параметры.
   * @return {@link CreditDto} объект, содержащий рассчитанные параметры кредита.
   */
  public CreditDto calculateCredit(ScoringDataDto scoringData) {
    log.debug("Starting the calculation of all credit conditions {}", scoringData.toString());
    BigDecimal rate = scoringService.calculateRate(scoringData);

    BigDecimal totalAmount =
        scoringService.calculateTotalAmount(
            scoringData.getAmount(), scoringData.getIsInsuranceEnabled());

    BigDecimal monthlyPayment =
        scoringService.calculateMonthlyPayment(totalAmount, rate, scoringData.getTerm());

    BigDecimal psk =
        scoringService.calculatePsk(totalAmount, monthlyPayment, scoringData.getTerm());

    List<PaymentScheduleElementDto> paymentSchedule =
        scoringService.calculatePaymentSchedule(
            totalAmount, rate, scoringData.getTerm(), monthlyPayment);

    CreditDto creditDto =
        CreditDto.builder()
            .amount(totalAmount)
            .term(scoringData.getTerm())
            .monthlyPayment(monthlyPayment)
            .rate(rate)
            .psk(psk)
            .isInsuranceEnabled(scoringData.getIsInsuranceEnabled())
            .isSalaryClient(scoringData.getIsSalaryClient())
            .paymentSchedule(paymentSchedule)
            .build();

    log.debug("The credit terms have been calculated {}", creditDto.toString());
    return creditDto;
  }
}
