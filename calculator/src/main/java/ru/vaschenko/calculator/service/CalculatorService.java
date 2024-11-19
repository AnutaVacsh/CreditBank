package ru.vaschenko.calculator.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vaschenko.calculator.dto.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CalculatorService {
    BaseCalculatorService baseCalculatorService;

    public CreditDto calculateCredit(ScoringDataDto scoringData) {
        log.info("Начинаем расчёт всех условий кредита");
        // Расчет процентной ставки
        BigDecimal rate = baseCalculatorService.calculateRate(scoringData);

        // Расчет полной суммы кредита (со строховкой)
        BigDecimal totalAmount = baseCalculatorService.calculateTotalAmount(scoringData.getAmount(), scoringData.getIsInsuranceEnabled());

        // Расчет ежемесячного платежа
        BigDecimal monthlyPayment = baseCalculatorService.calculateMonthlyPayment(totalAmount, rate, scoringData.getTerm());

        // Расчёт полной суммы кредита
        BigDecimal psk = baseCalculatorService.calculatePsk(totalAmount, monthlyPayment, scoringData.getTerm());

        // Расчет графика платежей
        List<PaymentScheduleElementDto> paymentSchedule = baseCalculatorService.calculatePaymentSchedule(totalAmount, rate, scoringData.getTerm(), monthlyPayment);

        CreditDto creditDto = new CreditDto();
        creditDto.setAmount(totalAmount);
        creditDto.setTerm(scoringData.getTerm());
        creditDto.setMonthlyPayment(monthlyPayment);
        creditDto.setRate(rate);
        creditDto.setPsk(psk);
        creditDto.setIsInsuranceEnabled(scoringData.getIsInsuranceEnabled());
        creditDto.setIsSalaryClient(scoringData.getIsSalaryClient());
        creditDto.setPaymentSchedule(paymentSchedule);

        log.info("Расчёт завершён");
        return creditDto;
    }
}

