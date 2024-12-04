package ru.vaschenko.deal.models.json;

import java.time.LocalDate;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Passport {
  private UUID passportUUID;
  private String series;
  private String number;
  private String issueBranch;
  private LocalDate issueDate;
}
