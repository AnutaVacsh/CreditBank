package ru.vaschenko.statement.controllers;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
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
        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(BigDecimal.class, () -> BigDecimal.valueOf(50000))
                .randomize(String.class, () -> "Jon");

        EasyRandom easyRandom = new EasyRandom(parameters);
        loanStatementRequestDto = easyRandom.nextObject(LoanStatementRequestDto.class);
        loanOfferDto = easyRandom.nextObject(LoanOfferDto.class);
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