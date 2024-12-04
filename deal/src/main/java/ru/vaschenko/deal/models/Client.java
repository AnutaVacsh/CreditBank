package ru.vaschenko.deal.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.vaschenko.deal.models.enams.Gender;
import ru.vaschenko.deal.models.enams.MaritalStatus;
import ru.vaschenko.deal.models.json.Employment;
import ru.vaschenko.deal.models.json.Passport;

@Entity
@Table(name = "client")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Client {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  UUID clientId;

  @Column(nullable = false)
  String lastName;

  @Column(nullable = false)
  String firstName;

  String middleName;

  @Column(name = "birth_date", nullable = false)
  LocalDate birthdate;

  @Column(nullable = false)
  String email;

  @Enumerated(EnumType.STRING)
  Gender gender;

  @Enumerated(EnumType.STRING)
  MaritalStatus maritalStatus;

  Integer dependentAmount;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "passport_id", columnDefinition = "jsonb")
  Passport passport;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "employment_id", columnDefinition = "jsonb")
  Employment employment;

  String accountNumber;
}
