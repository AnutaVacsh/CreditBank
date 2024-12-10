package ru.vaschenko.deal.services.client.impl;

import feign.FeignException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.deal.dto.CreditDto;
import ru.vaschenko.deal.dto.LoanOfferDto;
import ru.vaschenko.deal.dto.LoanStatementRequestDto;
import ru.vaschenko.deal.dto.ScoringDataDto;
import ru.vaschenko.deal.exception.PrescoringException;
import ru.vaschenko.deal.exception.ScoringCalculationException;
import ru.vaschenko.deal.services.client.CalculatorClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculatorFacade implements CalculatorClient {
  private final CalculatorClient calculatorClient;

  @Override
  public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
    log.debug("Sending request to get loan offers with data {}", loanStatementRequestDto);
    try {
      log.info("Response from calculator service received successfully");
      return calculatorClient.getLoanOffers(loanStatementRequestDto);
    } catch (FeignException e) {
      if (e.status() == 400) {
        log.error("(Request failed due to invalid data error (400): {}", e.getMessage());
        throw new PrescoringException(e.getMessage());
      }
      log.error("Request to calculator service failed: {}", e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public CreditDto getCredit(ScoringDataDto scoringDataDto){
    log.debug("Sending request to get credit calculation with data: {}", scoringDataDto);
    try {
      log.info("Response from calculator service received successfully");
      return calculatorClient.getCredit(scoringDataDto);
    } catch (FeignException e) {
      if (e.status() == 400) {
        log.error("Request failed due to invalid data error (400): {}", e.getMessage());
        throw new ScoringCalculationException(e.getMessage());
      }
      log.error("Request to calculator service failed: {}", e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }
}
