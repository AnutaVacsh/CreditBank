package ru.vaschenko.deal.models.json;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.Accessors;
import ru.vaschenko.deal.models.enams.ChangeType;

@Data
@Accessors(chain = true)
public class StatusHistory {
  private String status;
  private LocalDateTime time;
  private ChangeType type;
}
