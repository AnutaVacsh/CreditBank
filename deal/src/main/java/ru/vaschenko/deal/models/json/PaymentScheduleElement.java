package ru.vaschenko.deal.models.json;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PaymentScheduleElement {
  private Integer number;
  private LocalDate date;
  private BigDecimal totalPayment;
  private BigDecimal interestPayment;
  private BigDecimal debtPayment;
  private BigDecimal remainingDebt;
}
