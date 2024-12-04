package ru.vaschenko.deal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import ru.vaschenko.deal.models.enams.EmploymentPosition;
import ru.vaschenko.deal.models.enams.EmploymentStatus;

@Data
@Builder
@Schema(description = "Client description")
public class EmploymentDto {

  @JsonProperty("employment_status")
  @Schema(description = "Employment status")
  private EmploymentStatus employmentStatus;

  @JsonProperty("employer_inn")
  @Schema(description = "Employer's INN", example = "7712345678")
  @Pattern(regexp = "\\d{10}", message = "Employer's INN must contain 10 digits")
  private String employerINN;

  @JsonProperty("salary")
  @Schema(description = "Salary amount", example = "75000.00")
  @DecimalMin(value = "0.00", inclusive = false, message = "Salary must be greater than 0")
  private BigDecimal salary;

  @JsonProperty("position")
  @Schema(description = "Position")
  private EmploymentPosition position;

  @JsonProperty("work_experience_total")
  @Schema(description = "Total work experience (in months)", example = "120")
  @Min(value = 0, message = "Total work experience cannot be negative")
  private Integer workExperienceTotal;

  @JsonProperty("work_experience_current")
  @Schema(description = "Current job experience (in months)", example = "24")
  @Min(value = 0, message = "Current job experience cannot be negative")
  private Integer workExperienceCurrent;
}
