package ru.vaschenko.deal.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.vaschenko.deal.dto.ErrorMessageDto;
import ru.vaschenko.deal.dto.FinishRegistrationRequestDto;
import ru.vaschenko.deal.dto.LoanOfferDto;
import ru.vaschenko.deal.dto.LoanStatementRequestDto;
import ru.vaschenko.deal.util.ApiPath;

/** API for managing loan applications */
@Tag(name = "Deal API", description = "API for managing loan applications")
public interface DealApi {
  /**
   * Create a loan application.
   *
   * @param loanStatementRequestDto The data for creating the application.
   * @return A list of loan offers from worst to best.
   */
  @Operation(
      summary = "Create loan application",
      description =
          "The API receives LoanStatementRequestDto. Based on the data, a client and application are created, and a "
              + "request is sent to the calculator to get loan offers.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Calculated offers",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = LoanOfferDto.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid format or prescoring error",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorMessageDto.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Server's error",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorMessageDto.class))
            })
      })
  @PostMapping(ApiPath.STATEMENT)
  ResponseEntity<List<LoanOfferDto>> createStatement(
      @RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto);

  /**
   * Select a loan offer.
   *
   * @param loanOfferDto The selected loan offer.
   */
  @Operation(
      summary = "Select loan offer",
      description = "Updates the application status, status history, and the selected loan offer.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Select offer"),
        @ApiResponse(
            responseCode = "404",
            description = "Statement not found",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorMessageDto.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Server's error",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorMessageDto.class))
            })
      })
  @PostMapping(ApiPath.OFFER_SELECT)
  void selectOffer(@RequestBody @Valid LoanOfferDto loanOfferDto);

  /**
   * Calculate loan details.
   *
   * @param statementId The application ID.
   * @param finishRegistrationRequestDto The calculation data.
   */
  @Operation(
      summary = "Loan application calculation",
      description =
          "Sends a request to the calculator for loan calculation with client data. A Credit entity is created and the "
              + "application is updated with the result.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Calculate and save credit"),
        @ApiResponse(
            responseCode = "404",
            description = "Statement not found",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorMessageDto.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Server's error or scoring error",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorMessageDto.class))
            })
      })
  @PostMapping(ApiPath.CALCULATE_ID)
  void calculate(
      @PathVariable UUID statementId,
      @RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto);
}
