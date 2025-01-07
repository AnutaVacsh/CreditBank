package ru.vaschenko.deal.controllers;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.vaschenko.deal.api.DealApi;
import ru.vaschenko.deal.dto.FinishRegistrationRequestDto;
import ru.vaschenko.deal.dto.LoanOfferDto;
import ru.vaschenko.deal.dto.LoanStatementRequestDto;
import ru.vaschenko.deal.models.enams.ApplicationStatus;
import ru.vaschenko.deal.services.DealServices;
import ru.vaschenko.deal.services.StatementService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DealController implements DealApi {
  private final DealServices dealServices;

  @Override
  public ResponseEntity<List<LoanOfferDto>> createStatement(
      LoanStatementRequestDto loanStatementRequestDto) {
    return dealServices.createStatement(loanStatementRequestDto);
  }

  @Override
  public void selectOffer(LoanOfferDto loanOfferDto) {
    dealServices.selectOffer(loanOfferDto);
  }

  @Override
  public void calculate(
      UUID statementId, FinishRegistrationRequestDto finishRegistrationRequestDto) {
    dealServices.calculate(statementId, finishRegistrationRequestDto);
  }

  @Override
  public void sendCodeDocument(UUID statementId) {
    dealServices.sendCodeDocument(statementId);
  }

  @Override
  public void signCodeDocument(UUID statementId) {
    dealServices.signCodeDocument(statementId);
  }

  @Override
  public void codeDocument(UUID statementId, String sesCode) {
    dealServices.codeDocument(statementId, sesCode);
  }

  @Override
  public void documentCreated(UUID statementId, ApplicationStatus status) {
    dealServices.updateStatus(statementId, status);
  }
}
