package ru.vaschenko.calculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import ru.vaschenko.calculator.dto.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculatorServiceTest {

    @Mock
    private BaseCalculatorService baseCalculatorService;

    @InjectMocks
    CalculatorService calculatorService;

    @Test
    void calculateCredit() {
        ScoringDataDto scoringData = new ScoringDataDto();
        scoringData.setAmount(new BigDecimal(1000000));
        scoringData.setTerm(24);
        scoringData.setIsInsuranceEnabled(true);
        scoringData.setIsSalaryClient(false);

        when(baseCalculatorService.calculateRate(scoringData)).thenReturn(BigDecimal.valueOf(12));

        when(baseCalculatorService.calculateTotalAmount(eq(new BigDecimal(1000000)), eq(true)))
                .thenReturn(new BigDecimal(1100000));

        when(baseCalculatorService.calculateMonthlyPayment(eq(new BigDecimal(1100000)), eq(new BigDecimal(12)), eq(24)))
                .thenReturn(BigDecimal.valueOf(51781));

        when(baseCalculatorService.calculatePsk(eq(new BigDecimal(1100000)), eq(new BigDecimal(51781)), eq(24)))
                .thenReturn(new BigDecimal("12.98").setScale(2, RoundingMode.HALF_UP));

        when(baseCalculatorService.calculatePaymentSchedule(eq(new BigDecimal(1100000)), eq(new BigDecimal(12)), eq(24), eq(new BigDecimal(51781))))
                .thenAnswer(invocation -> new ArrayList<PaymentScheduleElementDto>(Collections.nCopies(invocation.getArgument(2), new PaymentScheduleElementDto())));

        // Вызов тестируемого метода
        CreditDto credit = calculatorService.calculateCredit(scoringData);

        // Проверка результата
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

        // Проверка вызова методов
        verify(baseCalculatorService).calculateRate(scoringData);
        verify(baseCalculatorService).calculateTotalAmount(new BigDecimal(1000000), true);
        verify(baseCalculatorService).calculateMonthlyPayment(new BigDecimal(1100000), new BigDecimal(12), 24);
        verify(baseCalculatorService).calculatePsk(new BigDecimal(1100000), new BigDecimal(51781), 24);
        verify(baseCalculatorService).calculatePaymentSchedule(new BigDecimal(1100000), new BigDecimal(12), 24, new BigDecimal(51781));
    }
}