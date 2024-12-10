package ru.vaschenko.deal.models.json;

import java.time.LocalDate;
import java.util.UUID;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Passport {
  private UUID passportUUID;
  private String series;
  private String number;
  private String issueBranch;
  private LocalDate issueDate;
}
