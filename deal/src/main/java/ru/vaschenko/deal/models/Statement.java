package ru.vaschenko.deal.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.vaschenko.deal.dto.LoanOfferDto;
import ru.vaschenko.deal.models.enams.ApplicationStatus;
import ru.vaschenko.deal.models.enams.ChangeType;
import ru.vaschenko.deal.models.json.StatusHistory;

@Entity
@Table(name = "statements")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Statement {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  //    @Column(nullable = false)
  private UUID statementId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_id", nullable = false)
  private Client client;

  @ManyToOne
  @JoinColumn(name = "credit_id")
  private Credit credit;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ApplicationStatus status;

  @Column(nullable = false)
  private LocalDateTime creationDate;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private LoanOfferDto appliedOffer;

  private LocalDateTime signDate;

  private String sesCode;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private List<StatusHistory> statusHistory;

  /**
   * Устанавливает новый статус заявки с типом изменения по умолчанию {@link ChangeType#AUTOMATIC}.
   *
   * @param status новый статус заявки {@link ApplicationStatus}.
   */
  public void setStatus(ApplicationStatus status) {
    setStatus(status, ChangeType.AUTOMATIC);
  }

  /**
   * Устанавливает новый статус заявки и добавляет запись в историю изменений статуса.
   *
   * @param status новый статус заявки {@link ApplicationStatus}.
   * @param type тип изменения статуса (например, {@link ChangeType}).
   */
  public void setStatus(ApplicationStatus status, ChangeType type) {
    this.status = status;
    addStatusHistory(
        StatusHistory.builder().status(status.name()).time(LocalDate.now()).type(type).build());
  }

  /**
   * Добавляет новую запись в историю изменений статусов заявки.
   *
   * @param status объект {@link StatusHistory}, содержащий данные о статусе, времени изменения и
   *     типе.
   */
  public void addStatusHistory(StatusHistory status) {
    if (statusHistory == null) {
      statusHistory = new ArrayList<>();
    }
    statusHistory.add(status);
  }
}
