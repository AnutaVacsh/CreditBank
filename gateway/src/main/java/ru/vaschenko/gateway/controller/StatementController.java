package ru.vaschenko.gateway.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.vaschenko.gateway.api.StatementApi;
import ru.vaschenko.gateway.client.deal.DealFacade;
import ru.vaschenko.gateway.client.statement.StatementFacade;
import ru.vaschenko.gateway.dto.FinishRegistrationRequestDto;
import ru.vaschenko.gateway.dto.LoanOfferDto;
import ru.vaschenko.gateway.dto.LoanStatementRequestDto;

@RestController
@RequiredArgsConstructor
public class StatementController implements StatementApi {
  private final StatementFacade statementClient;
  private final DealFacade dealClient;

  @Override
  public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
    return statementClient.getLoanOffers(loanStatementRequestDto);
  }

  @Override
  public void selectOffer(LoanOfferDto loanOfferDto) {
    statementClient.selectOffer(loanOfferDto);
  }

  @Override
  public void calculate(
      UUID statementId, FinishRegistrationRequestDto finishRegistrationRequestDto) {
    dealClient.calculate(statementId, finishRegistrationRequestDto);
  }
}
