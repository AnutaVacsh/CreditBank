package ru.vaschenko.statement.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.vaschenko.statement.dto.LoanOfferDto;
import ru.vaschenko.statement.dto.LoanStatementRequestDto;
import ru.vaschenko.statement.service.StatementService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatementControllerTest {

    @Mock
    private StatementService statementService;

    @InjectMocks
    private StatementController statementController;

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
                .birthdate(LocalDate.of(1990, 5, 1))
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
    void testCalculateLoanOffers_Success() {
        // When
        statementController.calculateLoanOffers(loanStatementRequestDto);

        // Then
        verify(statementService, times(1)).calculateLoanOffers(loanStatementRequestDto);
    }

    @Test
    void testSelectOffer_Success() {
        // When
        statementController.selectOffer(loanOfferDto);

        // Then
        verify(statementService, times(1)).selectOffer(loanOfferDto);
    }
}