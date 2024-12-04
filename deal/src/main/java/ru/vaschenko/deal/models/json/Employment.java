package ru.vaschenko.deal.models.json;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.*;
import ru.vaschenko.deal.models.enams.EmploymentPosition;
import ru.vaschenko.deal.models.enams.EmploymentStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employment {
  private UUID employmentUUID;
  private EmploymentStatus status;
  private String inn;
  private BigDecimal salary;
  private EmploymentPosition position;
  private Integer workExperienceTotal;
  private Integer workExperienceCurrent;
}
