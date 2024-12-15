package ru.vaschenko.deal.client.impl;

import static java.time.LocalDate.parse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import feign.FeignException;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.vaschenko.deal.client.CalculatorClient;
import ru.vaschenko.deal.dto.CreditDto;
import ru.vaschenko.deal.dto.LoanOfferDto;
import ru.vaschenko.deal.dto.LoanStatementRequestDto;
import ru.vaschenko.deal.dto.ScoringDataDto;

@ExtendWith(MockitoExtension.class)
class CalculatorFacadeTest {

  @Mock private CalculatorClient calculatorClient;

  @InjectMocks private CalculatorFacade calculatorFacade;

  @Test
  void shouldReturnLoanOffers() {
    // Given
    LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder().build();

    List<LoanOfferDto> loanOffers = Collections.nCopies(4, LoanOfferDto.builder().build());
    when(calculatorClient.getLoanOffers(loanStatementRequestDto)).thenReturn(loanOffers);

    // When
    List<LoanOfferDto> result = calculatorFacade.getLoanOffers(loanStatementRequestDto);

    // Then
    assertNotNull(result);
    assertEquals(4, result.size());
    verify(calculatorClient, times(1)).getLoanOffers(loanStatementRequestDto);
  }

  @Test
  void shouldReturnCredit() {
    // Given
    ScoringDataDto scoringDataDto = ScoringDataDto.builder().build();
    CreditDto creditDto = CreditDto.builder().build();
    when(calculatorClient.getCredit(scoringDataDto)).thenReturn(creditDto);

    // When
    CreditDto result = calculatorFacade.getCredit(scoringDataDto);

    // Then
    assertNotNull(result);
    verify(calculatorClient, times(1)).getCredit(scoringDataDto);
  }
}
