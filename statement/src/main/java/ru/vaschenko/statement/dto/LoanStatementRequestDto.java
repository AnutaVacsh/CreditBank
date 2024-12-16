package ru.vaschenko.statement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(description = "Loan application request")
public class LoanStatementRequestDto {

  @JsonProperty("amount")
  @Schema(description = "Loan amount", example = "50000")
  @DecimalMin(value = "20000", message = "Loan amount must be greater than or equal to 20000")
  private BigDecimal amount;

  @JsonProperty("term")
  @Schema(description = "Loan term (in months)", example = "24")
  @Min(value = 6, message = "Loan term must be greater than or equal to 6 months")
  private Integer term;

  @JsonProperty("first_name")
  @Schema(description = "First name", example = "John")
  @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
  @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must only contain Latin letters")
  private String firstName;

  @JsonProperty("last_name")
  @Schema(description = "Last name", example = "Doe")
  @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters")
  @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must only contain Latin letters")
  private String lastName;

  @JsonProperty("middle_name")
  @Schema(description = "Middle name", example = "Ivanovich")
  @Size(min = 2, max = 30, message = "Middle name must be between 2 and 30 characters")
  @Pattern(regexp = "^[a-zA-Z]+$", message = "Middle name must only contain Latin letters")
  private String middleName;

  @JsonProperty("email")
  @Schema(description = "Email address", example = "example@example.com")
  @Email(message = "Invalid email format")
  private String email;

  @JsonProperty("birthdate")
  @Schema(description = "Date of birth", example = "1990-05-01")
  @Past(message = "Date of birth must be at least 18 years ago")
  private LocalDate birthdate;

  @JsonProperty("passport_series")
  @Schema(description = "Passport series", example = "1234")
  @Pattern(regexp = "\\d{4}", message = "Passport series must consist of 4 digits")
  private String passportSeries;

  @JsonProperty("passport_number")
  @Schema(description = "Passport number", example = "123456")
  @Pattern(regexp = "\\d{6}", message = "Passport number must consist of 6 digits")
  private String passportNumber;
}
