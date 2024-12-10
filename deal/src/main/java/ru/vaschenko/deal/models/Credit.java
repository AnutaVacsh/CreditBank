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
  private UUID creditId;

  private BigDecimal amount;

  private Integer term;

  private BigDecimal monthlyPayment;

  private BigDecimal rate;

  private BigDecimal psk;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "payment_schedule")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private List<PaymentScheduleElement> paymentSchedule = new ArrayList<>();

  private Boolean insuranceEnabled;

  private Boolean salaryClient;

  @Enumerated(EnumType.STRING)
  private CreditStatus creditStatus;
}
