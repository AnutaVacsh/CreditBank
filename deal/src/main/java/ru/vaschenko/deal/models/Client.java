package ru.vaschenko.deal.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.vaschenko.deal.models.enams.Gender;
import ru.vaschenko.deal.models.enams.MaritalStatus;
import ru.vaschenko.deal.models.json.Employment;
import ru.vaschenko.deal.models.json.Passport;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "client")
public class Client {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  UUID clientId;

  String lastName;

  String firstName;

  String middleName;

  @Column(name = "birth_date")
  LocalDate birthdate;

  String email;

  @Enumerated(EnumType.STRING)
  Gender gender;

  @Enumerated(EnumType.STRING)
  MaritalStatus maritalStatus;

  Integer dependentAmount;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "passport_id", columnDefinition = "jsonb")
  Passport passport;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "employment_id", columnDefinition = "jsonb")
  Employment employment;

  String accountNumber;
}
