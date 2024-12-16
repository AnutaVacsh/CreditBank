package ru.vaschenko.calculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vaschenko.calculator.dto.PaymentScheduleElementDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.scoring.RateAndOtherScoringDto;
import ru.vaschenko.calculator.exception.ScoringCalculationException;
import ru.vaschenko.calculator.service.proveders.ScoringProvider;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringService {
  @Value("${loan.base.rate:15}")
  public BigDecimal baseRate;

  @Value("${insurance.price:100000}")
  public BigDecimal insurancePrice;

  private final ScoringProvider scoringProvider;

  /**
   * Рассчитывает ставку на основе всех применяемых правил скоринга.
   * <p>
   * Метод вызывает {@link ScoringProvider#fullScoring(ScoringDataDto)} для выполнения полной
   * проверки заявки, которая включает как жесткие, так и мягкие критерии. После получения результата
   * расчета ставки, метод логирует информацию о вычисленной ставке и дополнительных услугах,
   * и возвращает рассчитанную ставку.
   * </p>
   *
   * @param scoringData объект, содержащий все данные для расчета ставки
   * @return рассчитанную ставку типа {@link BigDecimal}, которая основывается на правилах скоринга.
   * @throws ScoringCalculationException если при расчете ставки произошла ошибка, например, отказ по
   *                                     жестким критериям.
   */
  protected BigDecimal calculateRate(ScoringDataDto scoringData) {
    log.debug("Calculating the rate based on scoring rules {}", scoringData.toString());
    RateAndOtherScoringDto resultScoring = scoringProvider.fullScoring(scoringData);

    log.debug("result scoring {} {}", resultScoring.newRate(), resultScoring.otherService());
    return resultScoring.newRate();
  }
//
//  /**
//   * Прескоринг. Рассчитывает процентную ставку с учетом наличия страховки и зарплаты клиента.
//   * Формула: <br>
//   * rate = baseRate - (isInsuranceEnabled ? 3 : 0) - (isSalaryClient ? 1 : 0)
//   *
//   * @param isInsuranceEnabled наличие страховки.
//   * @param isSalaryClient зарплпта клиента.
//   * @return Процентная ставка.
//   */
//  protected BigDecimal calculateRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
//    BigDecimal rate = baseRate;
//
//    if (isInsuranceEnabled) rate = rate.subtract(BigDecimal.valueOf(3));
//
//    if (isSalaryClient) rate = rate.subtract(BigDecimal.valueOf(1));
//
//    log.debug("The rate has been calculated {}", rate);
//    return rate;
//  }

  /**
   * Рассчитывает общую сумму кредита с учетом страховки. Формула: <br>
   * totalAmount = amount + (isInsuranceEnabled ? insurancePrice : 0)
   *
   * @param amount Сумма кредита.
   * @param isInsuranceEnabled Признак наличия страховки.
   * @return Общая сумма кредита.
   */
  protected BigDecimal calculateTotalAmount(BigDecimal amount, Boolean isInsuranceEnabled) {
    if (isInsuranceEnabled) {
      return amount.add(insurancePrice);
    }

    log.info("The total loan amount has been calculated {}", amount);
    return amount;
  }

  /**
   * Рассчитывает сумму ежемесячного платежа по кредиту. Формула: <br>
   * PMT = P * r * (1 + r)^n / ((1 + r)^n - 1), <br>
   * где: <br>
   * PMT - ежемесячный платеж, <br>
   * P - сумма кредита, <br>
   * r - месячная процентная ставка, <br>
   * n - срок кредита в месяцах.
   *
   * @param totalAmount Общая сумма кредита.
   * @param rate Процентная ставка.
   * @param term Срок кредита в месяцах.
   * @return Ежемесячный платеж.
   */
  protected BigDecimal calculateMonthlyPayment(
      BigDecimal totalAmount, BigDecimal rate, Integer term) {
    BigDecimal monthlyRate =
        rate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_EVEN)
            .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_EVEN);

    double pow = Math.pow(1 + monthlyRate.doubleValue(), term);
    BigDecimal numerator = totalAmount.multiply(monthlyRate).multiply(BigDecimal.valueOf(pow));
    BigDecimal denominator = BigDecimal.valueOf(pow - 1);

    BigDecimal monthlyPayment = numerator.divide(denominator, 2, RoundingMode.HALF_EVEN);
    log.debug("The monthly payment amount has been calculated {}", monthlyPayment);
    return monthlyPayment;
  }

  /**
   * Формирует график ежемесячных платежей по кредиту. Для каждого месяца рассчитывается: <br>
   * - Проценты за месяц: {@code interestPayment} = remainingDebt * monthlyRate <br>
   * - Погашение тела кредита: {@code debtPayment} = monthlyPayment - interestPayment <br>
   * - Оставшийся долг: {@code remainingDebt} = remainingDebt - debtPayment <br>
   *
   * @param totalAmount Общая сумма кредита.
   * @param rate Процентная ставка.
   * @param term Срок кредита в месяцах.
   * @param monthlyPayment Ежемесячный платеж.
   * @return Список элементов графика платежей.
   */
  protected List<PaymentScheduleElementDto> calculatePaymentSchedule(
      BigDecimal totalAmount, BigDecimal rate, Integer term, BigDecimal monthlyPayment) {
    List<PaymentScheduleElementDto> schedule = new ArrayList<>();
    BigDecimal monthlyRate =
        rate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_EVEN)
            .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_EVEN);
    BigDecimal remainingDebt = totalAmount;

    for (int i = 1; i <= term; i++) {
      BigDecimal interestPayment =
          remainingDebt.multiply(monthlyRate).setScale(2, RoundingMode.HALF_EVEN);

      BigDecimal debtPayment =
          monthlyPayment.subtract(interestPayment).setScale(2, RoundingMode.HALF_EVEN);

      remainingDebt = remainingDebt.subtract(debtPayment).setScale(2, RoundingMode.HALF_EVEN);

      PaymentScheduleElementDto elementDto =
          PaymentScheduleElementDto.builder()
              .number(i)
              .date(LocalDate.now().plusMonths(i))
              .debtPayment(debtPayment)
              .interestPayment(interestPayment)
              .totalPayment(monthlyPayment)
              .remainingDebt(remainingDebt)
              .build();

      schedule.add(elementDto);
    }
    log.info("The monthly payment schedule for the loan has been generated.");
    return schedule;
  }

  /**
   * Рассчитывает полную стоимость кредита в процентах. Формула: <br>
   * PСК = (totalPayments - totalAmount) / totalAmount * 100
   *
   * @param totalAmount Общая сумма кредита.
   * @param monthlyPayment Ежемесячный платеж.
   * @param term Срок кредита в месяцах.
   * @return ПСК (полная стоимость кредита в процентах).
   */
  protected BigDecimal calculatePsk(
      BigDecimal totalAmount, BigDecimal monthlyPayment, Integer term) {
    BigDecimal totalPayments = monthlyPayment.multiply(BigDecimal.valueOf(term));
    log.info("Calculated the total loan amount");

    return totalPayments
        .subtract(totalAmount)
        .divide(totalAmount, 4, RoundingMode.HALF_EVEN)
        .multiply(BigDecimal.valueOf(100));
  }
}
