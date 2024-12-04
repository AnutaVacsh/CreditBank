package ru.vaschenko.deal.models.json;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentScheduleElement {
  private Integer number;
  private LocalDate date;
  private BigDecimal totalPayment;
  private BigDecimal interestPayment;
  private BigDecimal debtPayment;
  private BigDecimal remainingDebt;
}
