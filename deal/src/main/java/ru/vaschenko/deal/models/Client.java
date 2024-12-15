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
  @Column(name = "client_id")
  private UUID clientId;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "middle_name")
  private String middleName;

  @Column(name = "birth_date")
  private LocalDate birthdate;

  @Column(name = "email")
  private String email;

  @Column(name = "gender")
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column(name = "marital_status")
  @Enumerated(EnumType.STRING)
  private MaritalStatus maritalStatus;

  @Column(name = "dependent_amount")
  private Integer dependentAmount;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "passport_id", columnDefinition = "jsonb")
  private Passport passport;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "employment_id", columnDefinition = "jsonb")
  private Employment employment;


  @Column(name = "account_number")
  private String accountNumber;
}
