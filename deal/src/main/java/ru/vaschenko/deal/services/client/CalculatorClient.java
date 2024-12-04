package ru.vaschenko.deal.services.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vaschenko.deal.dto.CreditDto;
import ru.vaschenko.deal.dto.LoanOfferDto;
import ru.vaschenko.deal.dto.LoanStatementRequestDto;
import ru.vaschenko.deal.dto.ScoringDataDto;
import ru.vaschenko.deal.util.ApiPath;

@FeignClient(value = "calculator-mc", url = "${client.calculator.url}")
public interface CalculatorClient {
  @PostMapping(ApiPath.CLIENT_OFFERS)
  List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto);

  @PostMapping(ApiPath.CLIENT_CALC)
  CreditDto getCredit(ScoringDataDto scoringDataDto);
}
