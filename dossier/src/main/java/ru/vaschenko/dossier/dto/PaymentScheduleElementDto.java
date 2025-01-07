package ru.vaschenko.dossier.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
@Schema(description = "Payment schedule element")
public class PaymentScheduleElementDto {

  @JsonProperty("number")
  @Schema(description = "Payment number", example = "1")
  private Integer number;

  @JsonProperty("date")
  @Schema(description = "Payment date", example = "2024-12-01")
  private LocalDate date;

  @JsonProperty("total_payment")
  @Schema(description = "Total payment amount", example = "8500.50")
  private BigDecimal totalPayment;

  @JsonProperty("interest_payment")
  @Schema(description = "Interest payment amount", example = "1500.50")
  private BigDecimal interestPayment;

  @JsonProperty("debt_payment")
  @Schema(description = "Debt repayment amount", example = "7000.00")
  private BigDecimal debtPayment;

  @JsonProperty("remaining_debt")
  @Schema(description = "Remaining debt amount", example = "93000.00")
  private BigDecimal remainingDebt;
}
