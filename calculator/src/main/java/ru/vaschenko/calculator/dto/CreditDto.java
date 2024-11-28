package ru.vaschenko.calculator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Schema(description = "Условия кредита")
public class CreditDto {

  @JsonProperty("amount")
  @Schema(description = "Сумма кредита", example = "1000000")
  private BigDecimal amount;

  @JsonProperty("term")
  @Schema(description = "Срок кредита в месяцах", example = "24")
  private Integer term;

  @JsonProperty("monthly_payment")
  @Schema(description = "Ежемесячный платеж", example = "8500.50")
  private BigDecimal monthlyPayment;

  @JsonProperty("rate")
  @Schema(description = "Процентная ставка", example = "12")
  private BigDecimal rate;

  @JsonProperty("psk")
  @Schema(description = "Полная стоимость кредита", example = "13.2")
  private BigDecimal psk;

  @JsonProperty("is_insurance_enabled")
  @Schema(description = "Наличие страховки", example = "true")
  private Boolean isInsuranceEnabled;

  @JsonProperty("is_salary_client")
  @Schema(description = "Зарплатный клиент", example = "false")
  private Boolean isSalaryClient;

  @JsonProperty("payment_schedule")
  @Schema(
      description = "График платежей",
      example =
          "[{\"number\":1,\"date\":\"2024-12-01\",\"totalPayment\":8500.50,"
              + "\"interestPayment\":1500.50,\"debtPayment\":7000.00,\"remainingDebt\":93000.00}]")
  private List<PaymentScheduleElementDto> paymentSchedule;
}
