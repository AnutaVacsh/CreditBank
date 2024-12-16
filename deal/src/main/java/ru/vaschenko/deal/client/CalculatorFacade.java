package ru.vaschenko.deal.client;

import feign.FeignException;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.deal.annotation.FeignRetryable;
import ru.vaschenko.deal.dto.CreditDto;
import ru.vaschenko.deal.dto.LoanOfferDto;
import ru.vaschenko.deal.dto.LoanStatementRequestDto;
import ru.vaschenko.deal.dto.ScoringDataDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculatorFacade implements CalculatorClient {
  private final CalculatorClient calculatorClient;

  @Override
  @FeignRetryable
  public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
    log.debug("Sending request to get loan offers with data {}", loanStatementRequestDto);
    return executeRequest(() -> calculatorClient.getLoanOffers(loanStatementRequestDto));
  }

  @Override
  @FeignRetryable
  public CreditDto getCredit(ScoringDataDto scoringDataDto) {
    log.debug("Sending request to get credit calculation with data: {}", scoringDataDto);
    return executeRequest(() -> calculatorClient.getCredit(scoringDataDto));
  }

  private <T> T executeRequest(Supplier<T> executor) {
    try {
      T result = executor.get();
      log.debug("Response from calculator service received successfully, return data {}", result);
      return result;
    } catch (FeignException e) {
      log.error("Request failed due to error: {}", e.getMessage());
      throw e;
    }
  }
}
