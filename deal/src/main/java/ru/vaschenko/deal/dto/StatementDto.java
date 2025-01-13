package ru.vaschenko.deal.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import ru.vaschenko.deal.models.enams.ApplicationStatus;
import ru.vaschenko.deal.models.json.StatusHistory;

@Data
@Builder
public class StatementDto {
  private UUID statementId;
  private ClientDto clientDto;
  private CreditDto creditDto;
  private ApplicationStatus status;
  private LocalDateTime creationDate;
  private LoanOfferDto appliedOffer;
  private LocalDateTime signDate;
  private String sesCode;
  private List<StatusHistory> statusHistory = new ArrayList<>();
}
