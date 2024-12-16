package ru.vaschenko.deal.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.vaschenko.deal.models.enams.CreditStatus;
import ru.vaschenko.deal.models.json.PaymentScheduleElement;

@Entity
@Table(name = "credit")
@Data
@Accessors(chain = true)
public class Credit {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "credit_id")
  private UUID creditId;

  @Column(name = "amount")
  private BigDecimal amount;

  @Column(name = "term")
  private Integer term;

  @Column(name = "monthly_payment")
  private BigDecimal monthlyPayment;

  @Column(name = "rate")
  private BigDecimal rate;

  @Column(name = "psk")
  private BigDecimal psk;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "payment_schedule")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<PaymentScheduleElement> paymentSchedule = new ArrayList<>();

  @Column(name = "insurance_enabled")
  private Boolean insuranceEnabled;

  @Column(name = "salary_client")
  private Boolean salaryClient;

  @Enumerated(EnumType.STRING)
  @Column(name = "credit_status")
  private CreditStatus creditStatus;
}
