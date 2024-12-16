package ru.vaschenko.calculator.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.vaschenko.calculator.api.CalculatorApi;
import ru.vaschenko.calculator.dto.CreditDto;
import ru.vaschenko.calculator.dto.LoanOfferDto;
import ru.vaschenko.calculator.dto.LoanStatementRequestDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.service.CalculatorService;
import ru.vaschenko.calculator.service.OfferService;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class CalculatorController implements CalculatorApi {
  private final CalculatorService calculatorService;
  private final OfferService offerService;

  @Override
  public ResponseEntity<List<LoanOfferDto>> calculateLoanOffers(
          @Valid LoanStatementRequestDto requestDto) {
    List<LoanOfferDto> offers = offerService.generateLoanOffers(requestDto);
    return ResponseEntity.ok(offers);
  }

  @Override
  public ResponseEntity<CreditDto> calculateCredit(@Valid ScoringDataDto scoringData) {
    CreditDto creditDto = calculatorService.calculateCredit(scoringData);
    return ResponseEntity.status(HttpStatus.CREATED).body(creditDto);
  }
}
