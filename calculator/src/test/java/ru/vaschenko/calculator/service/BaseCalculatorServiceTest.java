package ru.vaschenko.calculator.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vaschenko.calculator.dto.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class BaseCalculatorServiceTest {
    @InjectMocks
    private BaseCalculatorService baseCalculatorService;

    @BeforeEach
    void setUp() {
        baseCalculatorService.baseRate = BigDecimal.valueOf(15);
        baseCalculatorService.insurancePrice = BigDecimal.valueOf(100000);
    }

    @Test
    void calculateRate() {
        assertEquals(new BigDecimal(11), baseCalculatorService.calculateRate(true, true));
        assertEquals(new BigDecimal(12), baseCalculatorService.calculateRate(true, false));
        assertEquals(new BigDecimal(14), baseCalculatorService.calculateRate(false, true));
        assertEquals(new BigDecimal(15), baseCalculatorService.calculateRate(false, false));
    }

    @Test
    void calculateTotalAmount() {
        assertEquals(new BigDecimal(1100000), baseCalculatorService.calculateTotalAmount(new BigDecimal(1000000), true));
        assertEquals(new BigDecimal(1000000), baseCalculatorService.calculateTotalAmount(new BigDecimal(1000000), false));
    }

    @Test
    void calculateMonthlyPayment() {
        assertEquals(new BigDecimal("48486.65"), baseCalculatorService
                .calculateMonthlyPayment(new BigDecimal(1000000), new BigDecimal(15), 24));
        assertEquals(new BigDecimal("51268.62"), baseCalculatorService
                .calculateMonthlyPayment(new BigDecimal(1100000), new BigDecimal(11), 24));
    }

    @Test
    void calculatePaymentSchedule() {
        List<PaymentScheduleElementDto> listElement = baseCalculatorService
                .calculatePaymentSchedule(new BigDecimal(1000000), new BigDecimal(14), 24, new BigDecimal("48012.88"));

        assertEquals(24, listElement.size());
        assertEquals(new BigDecimal("736496.51"), listElement.get(6).getRemainingDebt());
        assertEquals(new BigDecimal("40816.29"), listElement.get(10).getDebtPayment());
    }

    @Test
    void calculatePsk() {
        assertEquals(new BigDecimal("16.37"), baseCalculatorService.calculatePsk(new BigDecimal(1000000), new BigDecimal("48486.65"), 24).setScale(2, RoundingMode.HALF_UP));
    }
}