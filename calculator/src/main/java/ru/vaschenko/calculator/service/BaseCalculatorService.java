package ru.vaschenko.calculator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vaschenko.calculator.dto.PaymentScheduleElementDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BaseCalculatorService {
    @Value("${loan.base.rate:15}")
    public BigDecimal baseRate;

    @Value("${insurance.price:100000}")
    public BigDecimal insurancePrice;

    public BigDecimal calculateRate(ScoringDataDto scoringData) {
        return calculateRate(scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient());
    }

    public BigDecimal calculateRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        log.info("Расчитываем ставку");
        BigDecimal rate = baseRate;

        if (isInsuranceEnabled)
            rate = rate.subtract(BigDecimal.valueOf(3)); // уменьшение ставки на 3% при наличии страховки

        if (isSalaryClient)
            rate = rate.subtract(BigDecimal.valueOf(1)); // уменьшение ставки на 1% для зарплатных клиентов

        return rate;
    }

    public BigDecimal calculateTotalAmount(BigDecimal amount, Boolean isInsuranceEnabled) {
        log.info("Расчитывает общую сумму кредита");
        if (isInsuranceEnabled) {
            return amount.add(insurancePrice);
        }
        return amount;
    }

    public BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, BigDecimal rate, Integer term) {
        log.info("Расчитывает сумму ежемесячного платежа");
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_EVEN).divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_EVEN);

        double pow = Math.pow(1 + monthlyRate.doubleValue(), term);
        BigDecimal numerator = totalAmount.multiply(monthlyRate).multiply(BigDecimal.valueOf(pow));
        BigDecimal denominator = BigDecimal.valueOf(pow - 1);

        return numerator.divide(denominator, 2, RoundingMode.HALF_EVEN); // Возвращаем результат с округлением до 2 знаков
    }

    public List<PaymentScheduleElementDto> calculatePaymentSchedule(BigDecimal totalAmount, BigDecimal rate, Integer term, BigDecimal monthlyPayment) {
        log.info("Формируем график ежемесячных платежей по кредиту");
        List<PaymentScheduleElementDto> schedule = new ArrayList<>();
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_EVEN).divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_EVEN);
        BigDecimal remainingDebt = totalAmount;

        for (int i = 1; i <= term; i++) {
            // Расчет процентов за месяц
            BigDecimal interestPayment = remainingDebt.multiply(monthlyRate).setScale(2, RoundingMode.HALF_EVEN);

            // Погашение тела кредита
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment).setScale(2, RoundingMode.HALF_EVEN);

            // Оставшийся долг
            remainingDebt = remainingDebt.subtract(debtPayment).setScale(2, RoundingMode.HALF_EVEN);

            PaymentScheduleElementDto element = new PaymentScheduleElementDto();
            element.setNumber(i);
            element.setDate(LocalDate.now().plusMonths(i));
            element.setTotalPayment(monthlyPayment);
            element.setInterestPayment(interestPayment);
            element.setDebtPayment(debtPayment);
            element.setRemainingDebt(remainingDebt);

            schedule.add(element);
            log.info("{} месяц добавлен в график", i);
        }
        return schedule;
    }

    public BigDecimal calculatePsk(BigDecimal totalAmount, BigDecimal monthlyPayment, Integer term) {
        log.info("Расчитываем полную сумму кредита");
        // Общая сумма платежей за весь срок
        BigDecimal totalPayments = monthlyPayment.multiply(BigDecimal.valueOf(term));

        // Расчет ПСК как процент от полной суммы кредита
        return totalPayments.subtract(totalAmount)
                .divide(totalAmount, 4, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal.valueOf(100));
    }
}
