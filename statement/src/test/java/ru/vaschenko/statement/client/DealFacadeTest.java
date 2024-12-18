package ru.vaschenko.statement.client;

import org.junit.jupiter.api.Test;


import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vaschenko.statement.dto.LoanOfferDto;
import ru.vaschenko.statement.dto.LoanStatementRequestDto;
import ru.vaschenko.statement.client.DealClient;
import ru.vaschenko.statement.client.DealFacade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.time.LocalDate.parse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealFacadeTest {

    @Mock
    private DealClient dealClient;

    @InjectMocks
    private DealFacade dealFacade;

    private LoanStatementRequestDto loanStatementRequestDto;
    private LoanOfferDto loanOfferDto;

    @BeforeEach
    void setUp() {
        loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(50000))
                .term(24)
                .firstName("John")
                .lastName("Doe")
                .middleName("Ivanovich")
                .email("example@example.com")
                .birthdate(parse("1990-05-01"))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        loanOfferDto = LoanOfferDto.builder()
                .requestedAmount(BigDecimal.valueOf(500000))
                .totalAmount(BigDecimal.valueOf(600000))
                .term(36)
                .monthlyPayment(BigDecimal.valueOf(16666.67))
                .rate(BigDecimal.valueOf(11))
                .isInsuranceEnabled(true)
                .isSalaryClient(false)
                .build();
    }

    @Test
    void shouldReturnLoanOffers() {
        // Given
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder().build();

        List<LoanOfferDto> loanOffers = Collections.nCopies(4, LoanOfferDto.builder().build());
        when(dealClient.getLoanOffers(loanStatementRequestDto)).thenReturn(loanOffers);

        // When
        List<LoanOfferDto> result = dealFacade.getLoanOffers(loanStatementRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(4, result.size());
        verify(dealClient, times(1)).getLoanOffers(loanStatementRequestDto);
    }

    @Test
    void shouldSelectLoanOffer() {
        // When
        dealFacade.selectOffer(loanOfferDto);

        // Then
        verify(dealClient, times(1)).selectOffer(loanOfferDto);
    }
}