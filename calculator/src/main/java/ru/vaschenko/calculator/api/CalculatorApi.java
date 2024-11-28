package ru.vaschenko.calculator.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.vaschenko.calculator.dto.*;
import ru.vaschenko.calculator.util.ApiPath;

import java.util.List;

@Tag(name = "Calculator", description = "Calculator service for credit conditions")
public interface CalculatorApi {

  @Operation(summary = "Calculate loan offers")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully calculated loan offers"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageDto.class))
            })
      })
  @PostMapping(ApiPath.CALCULATOR_OFFERS)
  ResponseEntity<List<LoanOfferDto>> calculateLoanOffers(
      @RequestBody @Valid LoanStatementRequestDto requestDto);

  @Operation(summary = "Calculate credit conditions")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Successfully calculated credit conditions"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessageDto.class))
            })
      })
  @PostMapping(ApiPath.CALCULATOR_CALC)
  ResponseEntity<CreditDto> calculateCredit(@RequestBody @Valid ScoringDataDto scoringData);
}
