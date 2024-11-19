package ru.vaschenko.calculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.anyBoolean;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vaschenko.calculator.dto.LoanOfferDto;
import ru.vaschenko.calculator.dto.LoanStatementRequestDto;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {
    @Mock
    private BaseCalculatorService baseCalculatorService;

    @InjectMocks
    OfferService offerService;

    @Test
    void generateLoanOffers() {
        LoanStatementRequestDto request = new LoanStatementRequestDto();
        request.setAmount(new BigDecimal(1000000));
        request.setTerm(24);

        when(baseCalculatorService.calculateRate(false, false)).thenReturn(BigDecimal.valueOf(15));
        when(baseCalculatorService.calculateRate(false, true)).thenReturn(BigDecimal.valueOf(14));
        when(baseCalculatorService.calculateRate(true, false)).thenReturn(BigDecimal.valueOf(12));
        when(baseCalculatorService.calculateRate(true, true)).thenReturn(BigDecimal.valueOf(11));

        //если страховки нет, то общая сумма кредита равна сумме кредита
        when(baseCalculatorService.calculateTotalAmount(any(BigDecimal.class), eq(false)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        //если страховка есть, то общая сумма кредита равна сумма кредита + страховка
        when(baseCalculatorService.calculateTotalAmount(any(BigDecimal.class), eq(true)))
                .thenAnswer(invocation -> ((BigDecimal) invocation.getArgument(0)).add(BigDecimal.valueOf(100000)));

        when(baseCalculatorService.calculateMonthlyPayment(eq(new BigDecimal(1000000)), eq(new BigDecimal(15)), eq(24)))
                .thenReturn(BigDecimal.valueOf(48487));
        when(baseCalculatorService.calculateMonthlyPayment(eq(new BigDecimal(1000000)), eq(new BigDecimal(14)), eq(24)))
                .thenReturn(BigDecimal.valueOf(48013));
        when(baseCalculatorService.calculateMonthlyPayment(eq(new BigDecimal(1100000)), eq(new BigDecimal(12)), eq(24)))
                .thenReturn(BigDecimal.valueOf(51781));
        when(baseCalculatorService.calculateMonthlyPayment(eq(new BigDecimal(1100000)), eq(new BigDecimal(11)), eq(24)))
                .thenReturn(BigDecimal.valueOf(51269));

        List<LoanOfferDto> loanOffers = offerService.generateLoanOffers(request);

        assertEquals(4, loanOffers.size());
        verify(baseCalculatorService, times(4)).calculateRate(anyBoolean(), anyBoolean());
        verify(baseCalculatorService, times(4)).calculateTotalAmount(any(BigDecimal.class), anyBoolean());
        verify(baseCalculatorService, times(4)).calculateMonthlyPayment(any(BigDecimal.class), any(BigDecimal.class), any(Integer.class));

        //проверяем правильность сортировки и расчёта процентной ставки
        assertEquals(BigDecimal.valueOf(11), loanOffers.get(3).getRate());
        assertEquals(BigDecimal.valueOf(12), loanOffers.get(2).getRate());
        assertEquals(BigDecimal.valueOf(14), loanOffers.get(1).getRate());
        assertEquals(BigDecimal.valueOf(15), loanOffers.get(0).getRate());

        //проверяем что все важные параметры почситались и вернулись
        for (LoanOfferDto offer : loanOffers) {
            assertNotNull(offer.getRate());
            assertNotNull(offer.getTotalAmount());
            assertNotNull(offer.getMonthlyPayment());
        }

        // Проверка конкретного передложения
        LoanOfferDto offer1 = loanOffers.get(0);
        assertEquals(BigDecimal.valueOf(1000000), offer1.getRequestedAmount());
        assertEquals(BigDecimal.valueOf(48487), offer1.getMonthlyPayment());
        assertEquals(24, offer1.getTerm());
    }
}