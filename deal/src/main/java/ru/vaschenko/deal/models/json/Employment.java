package ru.vaschenko.deal.models.json;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.*;
import lombok.experimental.Accessors;
import ru.vaschenko.deal.models.enams.EmploymentPosition;
import ru.vaschenko.deal.models.enams.EmploymentStatus;

@Data
@Accessors(chain = true)
public class Employment {
  private UUID employmentUUID;
  private EmploymentStatus status;
  private String inn;
  private BigDecimal salary;
  private EmploymentPosition position;
  private Integer workExperienceTotal;
  private Integer workExperienceCurrent;
}
