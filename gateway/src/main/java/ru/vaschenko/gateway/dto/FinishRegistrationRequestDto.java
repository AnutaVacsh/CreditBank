package ru.vaschenko.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import ru.vaschenko.gateway.dto.enums.Gender;
import ru.vaschenko.gateway.dto.enums.MaritalStatus;

@Data
@Builder
@Schema(description = "DTO для завершения регистрации")
public class FinishRegistrationRequestDto {

  @Schema(description = "Пол (Gender)")
  private Gender gender;

  @Schema(description = "Семейное положение (MaritalStatus)")
  private MaritalStatus maritalStatus;

  @Schema(description = "Количество иждивенцев", example = "2")
  private Integer dependentAmount;

  @Schema(description = "Дата выдачи паспорта", example = "2022-01-15")
  private LocalDate passportIssueDate;

  @Schema(description = "Отделение выдачи паспорта", example = "770-001")
  private String passportIssueBranch;

  @Schema(description = "Данные о занятости", implementation = EmploymentDto.class)
  private EmploymentDto employment;

  @Schema(description = "Номер банковского счёта", example = "40817810099910004312")
  private String accountNumber;
}
