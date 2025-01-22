package ru.vaschenko.deal.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import ru.vaschenko.deal.models.enams.Gender;
import ru.vaschenko.deal.models.enams.MaritalStatus;
import ru.vaschenko.deal.models.json.Employment;
import ru.vaschenko.deal.models.json.Passport;

@Data
@Builder
public class ClientDto {
  private UUID clientId;
  private String lastName;
  private String firstName;
  private String middleName;
  private LocalDate birthdate;
  private String email;
  private Gender gender;
  private MaritalStatus maritalStatus;
  private Integer dependentAmount;
  private Passport passport;
  private Employment employment;
  private String accountNumber;
}
