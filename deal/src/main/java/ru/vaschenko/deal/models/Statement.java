package ru.vaschenko.deal.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.vaschenko.deal.dto.LoanOfferDto;
import ru.vaschenko.deal.models.enams.ApplicationStatus;
import ru.vaschenko.deal.models.enams.ChangeType;
import ru.vaschenko.deal.models.json.StatusHistory;

@Entity
@Table(name = "statements")
@Data
@Accessors(chain = true)
public class Statement {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID statementId;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_id")
  private Client client;

  @OneToOne
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @JoinColumn(name = "credit_id")
  private Credit credit;

  @Enumerated(EnumType.STRING)
  private ApplicationStatus status;

  private LocalDateTime creationDate;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private LoanOfferDto appliedOffer;

  private LocalDateTime signDate;

  private String sesCode;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private List<StatusHistory> statusHistory = new ArrayList<>();

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
        new StatusHistory().setStatus(status.name()).setTime(LocalDateTime.now()).setType(type));
  }

  /**
   * Добавляет новую запись в историю изменений статусов заявки.
   *
   * @param status объект {@link StatusHistory}, содержащий данные о статусе, времени изменения и
   *     типе.
   */
  public void addStatusHistory(StatusHistory status) {
    statusHistory.add(status);
  }
}
