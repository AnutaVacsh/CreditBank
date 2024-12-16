package ru.vaschenko.deal.services;

import java.util.List;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vaschenko.deal.dto.CreditDto;
import ru.vaschenko.deal.dto.LoanOfferDto;
import ru.vaschenko.deal.dto.LoanStatementRequestDto;
import ru.vaschenko.deal.dto.ScoringDataDto;
import ru.vaschenko.deal.exception.PrescoringException;
import ru.vaschenko.deal.exception.ScoringCalculationException;
import ru.vaschenko.deal.models.Statement;
import ru.vaschenko.deal.models.enams.ApplicationStatus;
import ru.vaschenko.deal.client.CalculatorFacade;

@Service
@RequiredArgsConstructor
public class CalculatorService {
  private final CalculatorFacade calculatorClient;
  private final StatementService statementService;

  public List<LoanOfferDto> getLoanOffers(
      LoanStatementRequestDto loanStatementRequestDto, Statement statement) {
    try {
      return calculatorClient.getLoanOffers(loanStatementRequestDto);
    } catch (FeignException e) {
      statement.setStatus(ApplicationStatus.CC_DENIED);
      statementService.saveStatement(statement);
      throw new PrescoringException(e.getMessage());
    }
  }

  public CreditDto getCredit(ScoringDataDto scoringDataDto, Statement statement) {
    try {
      statement.setStatus(ApplicationStatus.CC_APPROVED);
      statementService.saveStatement(statement);
      return calculatorClient.getCredit(scoringDataDto);
    } catch (FeignException e) {
      statement.setStatus(ApplicationStatus.CC_DENIED);
      statementService.saveStatement(statement);
      throw new ScoringCalculationException(e.getMessage());
    }
  }
}
