package ru.vaschenko.deal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import ru.vaschenko.deal.models.enams.Gender;
import ru.vaschenko.deal.models.enams.MaritalStatus;

@Data
@Builder
@Schema(description = "Данные скоринга")
public class ScoringDataDto {

  @JsonProperty("amount")
  @Schema(description = "Requested loan amount", example = "100000")
  @DecimalMin(value = "20000", message = "The loan amount must be greater than or equal to 20000")
  private BigDecimal amount;

  @JsonProperty("term")
  @Schema(description = "Loan term in months", example = "24")
  @Min(value = 6, message = "The loan term must be greater than or equal to 6 months")
  private Integer term;

  @JsonProperty("first_name")
  @Schema(description = "Client's first name", example = "Ivan")
  @Pattern(
      regexp = "^[a-zA-Z]{2,30}$",
      message =
          "First name must contain only Latin letters and be between 2 and 30 characters long")
  private String firstName;

  @JsonProperty("last_name")
  @Schema(description = "Client's last name", example = "Ivanov")
  @Pattern(
      regexp = "^[a-zA-Z]{2,30}$",
      message = "Last name must contain only Latin letters and be between 2 and 30 characters long")
  private String lastName;

  @JsonProperty("middle_name")
  @Schema(description = "Client's middle name", example = "Ivanovich")
  @Pattern(
      regexp = "^[a-zA-Z]{2,30}$",
      message =
          "Middle name must contain only Latin letters and be between 2 and 30 characters long")
  private String middleName;

  @JsonProperty("gender")
  @Schema(description = "Client's gender")
  private Gender gender;

  @JsonProperty("birthdate")
  @Schema(description = "Client's birthdate", example = "1985-05-15")
  @Past(message = "The birthdate must be no later than 18 years from the current day")
  private LocalDate birthdate;

  @JsonProperty("passport_series")
  @Schema(description = "Client's passport series", example = "4500")
  @Pattern(regexp = "\\d{4}", message = "Passport series must contain 4 digits")
  private String passportSeries;

  @JsonProperty("passport_number")
  @Schema(description = "Client's passport number", example = "123456")
  @Pattern(regexp = "\\d{6}", message = "Passport number must contain 6 digits")
  private String passportNumber;

  @JsonProperty("passport_issue_date")
  @Schema(description = "Passport issue date", example = "2015-08-20")
  private LocalDate passportIssueDate;

  @JsonProperty("passport_issue_branch")
  @Schema(description = "Passport issuing authority code", example = "770-001")
  private String passportIssueBranch;

  @JsonProperty("marital_status")
  @Schema(description = "Client's marital status")
  private MaritalStatus maritalStatus;

  @JsonProperty("dependent_amount")
  @Schema(description = "Number of dependents", example = "2")
  @Min(value = 0, message = "Number of dependents cannot be negative")
  private Integer dependentAmount;

  @JsonProperty("employment")
  @Schema(description = "Client's employment data")
  private EmploymentDto employment;

  @JsonProperty("account_number")
  @Schema(description = "Client's account number", example = "40817810099910004312")
  private String accountNumber;

  @JsonProperty("is_insurance_enabled")
  @Schema(description = "Insurance availability", example = "true")
  private Boolean isInsuranceEnabled;

  @JsonProperty("is_salary_client")
  @Schema(description = "Salary client status", example = "false")
  private Boolean isSalaryClient;
}
