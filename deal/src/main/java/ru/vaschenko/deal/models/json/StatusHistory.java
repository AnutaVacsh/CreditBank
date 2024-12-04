package ru.vaschenko.deal.models.json;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.vaschenko.deal.models.enams.ChangeType;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusHistory {
  private String status;
  private LocalDate time;
  private ChangeType type;
}
