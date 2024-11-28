package ru.vaschenko.calculator.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.vaschenko.calculator.dto.CreditDto;
import ru.vaschenko.calculator.dto.LoanOfferDto;
import ru.vaschenko.calculator.dto.LoanStatementRequestDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.service.CalculatorService;
import ru.vaschenko.calculator.service.OfferService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerTest {

  @Mock private CalculatorService calculatorService;

  @Mock private OfferService offerService;

  @InjectMocks private CalculatorController calculatorController;

  @Test
  void calculateLoanOffers() {
    LoanStatementRequestDto requestDto =
        LoanStatementRequestDto.builder().amount(new BigDecimal("100000")).term(12).build();

    LoanOfferDto loanOffer =
        LoanOfferDto.builder()
            .totalAmount(new BigDecimal("120000"))
            .rate(new BigDecimal("7.5"))
            .build();

    when(offerService.generateLoanOffers(requestDto))
        .thenReturn(Collections.singletonList(loanOffer));

    ResponseEntity<List<LoanOfferDto>> response =
        calculatorController.calculateLoanOffers(requestDto);

    verify(offerService).generateLoanOffers(requestDto);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals(new BigDecimal("120000"), response.getBody().get(0).getTotalAmount());
  }

  @Test
  void calculateCredit() {
    ScoringDataDto scoringData =
        ScoringDataDto.builder()
            .amount(new BigDecimal("100000"))
            .term(12)
            .isInsuranceEnabled(true)
            .isSalaryClient(false)
            .build();

    CreditDto creditDto =
        CreditDto.builder()
            .amount(new BigDecimal("100000"))
            .term(12)
            .monthlyPayment(new BigDecimal("11000"))
            .rate(new BigDecimal("12"))
            .psk(new BigDecimal("15.5"))
            .build();

    when(calculatorService.calculateCredit(scoringData)).thenReturn(creditDto);

    ResponseEntity<CreditDto> response = calculatorController.calculateCredit(scoringData);

    verify(calculatorService).calculateCredit(scoringData);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(new BigDecimal("100000"), response.getBody().getAmount());
    assertEquals(new BigDecimal("11000"), response.getBody().getMonthlyPayment());
  }
}
