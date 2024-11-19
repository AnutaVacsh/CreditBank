package ru.vaschenko.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vaschenko.calculator.dto.CreditDto;
import ru.vaschenko.calculator.dto.LoanOfferDto;
import ru.vaschenko.calculator.dto.LoanStatementRequestDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.service.CalculatorService;
import ru.vaschenko.calculator.service.OfferService;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "Calculator", description = "Calculator service for credit conditions")
@RequestMapping("/calculator")
@AllArgsConstructor
public class CalculatorController {
    private final CalculatorService calculatorService;
    private final OfferService offerService;

    @Operation(summary = "Calculate loan offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated loan offers"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> calculateLoanOffers(
            @RequestBody LoanStatementRequestDto requestDto) {
        log.info("Получены данные для расчета предложений");
        List<LoanOfferDto> offers = offerService.generateLoanOffers(requestDto);
        log.info("Расчет завершен. Возвращён список предложений");
        return ResponseEntity.ok(offers);
    }

    @Operation(summary = "Calculate credit conditions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully calculated credit conditions"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calculateCredit(
            @RequestBody ScoringDataDto scoringData) {
        log.info("Получены данные для расчета условий кредита");
        CreditDto creditDto = calculatorService.calculateCredit(scoringData);
        log.info("Расчет завершен. Возвращён результат");
        return ResponseEntity.status(HttpStatus.CREATED).body(creditDto);
    }

}
