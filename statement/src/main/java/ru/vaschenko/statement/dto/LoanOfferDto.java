package ru.vaschenko.statement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@Schema(description = "Loan offer")
public class LoanOfferDto {

  @JsonProperty("statement_id")
  @Schema(description = "Statement ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID statementId;

  @JsonProperty("requested_amount")
  @Schema(description = "Requested loan amount", example = "500000.00")
  private BigDecimal requestedAmount;

  @JsonProperty("total_amount")
  @Schema(description = "Total loan amount", example = "600000.00")
  private BigDecimal totalAmount;

  @JsonProperty("term")
  @Schema(description = "Loan term in months", example = "36")
  private Integer term;

  @JsonProperty("monthly_payment")
  @Schema(description = "Monthly payment", example = "16666.67")
  private BigDecimal monthlyPayment;

  @JsonProperty("rate")
  @Schema(description = "Interest rate", example = "11")
  private BigDecimal rate;

  @JsonProperty("is_insurance_enabled")
  @Schema(description = "Insurance inclusion", example = "true")
  private Boolean isInsuranceEnabled;

  @JsonProperty("is_salary_client")
  @Schema(description = "Salary client", example = "false")
  private Boolean isSalaryClient;
}
