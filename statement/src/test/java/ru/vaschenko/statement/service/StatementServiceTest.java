package ru.vaschenko.statement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vaschenko.statement.client.DealFacade;
import ru.vaschenko.statement.dto.LoanOfferDto;
import ru.vaschenko.statement.dto.LoanStatementRequestDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.parse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatementServiceTest {

    @Mock
    private DealFacade dealFacade;

    @InjectMocks
    private StatementService statementService;

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
    void testCalculateLoanOffers() {
        // Given
        List<LoanOfferDto> expectedOffers = Arrays.asList(loanOfferDto);
        when(dealFacade.getLoanOffers(loanStatementRequestDto)).thenReturn(expectedOffers);

        // When
        List<LoanOfferDto> actualOffers = statementService.calculateLoanOffers(loanStatementRequestDto);

        // Then
        verify(dealFacade, times(1)).getLoanOffers(loanStatementRequestDto);
        assertEquals(expectedOffers, actualOffers);
    }

    @Test
    void testSelectOffer() {
        // When
        statementService.selectOffer(loanOfferDto);

        // Then
        verify(dealFacade, times(1)).selectOffer(loanOfferDto);
    }
}