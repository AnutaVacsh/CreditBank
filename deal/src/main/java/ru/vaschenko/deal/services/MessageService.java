package ru.vaschenko.deal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.deal.dto.CreditDto;
import ru.vaschenko.deal.mapping.CreditMapper;
import ru.vaschenko.deal.models.Statement;
import ru.vaschenko.deal.models.enams.Theme;
import ru.vaschenko.deal.services.kafka.producer.KafkaDealProducer;

/**
 * Сервис для отправки уведомлений и запросов через Kafka в процессе обработки заявок. Включает
 * методы для уведомлений о завершении регистрации, отклонении кредита, создании и подписании
 * документов.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

  private final CreditMapper creditMapper;
  private final KafkaDealProducer dealProducer;

  /**
   * Отправляет уведомление о завершении регистрации.
   *
   * @param statement Заявка клиента, для которой отправляется уведомление.
   */
  public void finishRegistration(Statement statement) {
    dealProducer.sendFinishRegistrationRequestNotification(
        statement.getClient().getEmail(), Theme.FINISH_REGISTRATION, statement.getStatementId());
  }

  /**
   * Отправляет уведомление о том, что кредит отклонен.
   *
   * @param statement Заявка клиента, для которой отправляется уведомление.
   */
  public void creditDenied(Statement statement) {
    dealProducer.sendCreateDocumentRequestNotification(
        statement.getClient().getEmail(), Theme.CC_DENIED, statement.getStatementId());
  }

  /**
   * Отправляет уведомление о создании документа для заявки.
   *
   * @param statement Заявка клиента, для которой отправляется уведомление.
   */
  public void createDocument(Statement statement) {
    dealProducer.sendCreateDocumentRequestNotification(
        statement.getClient().getEmail(), Theme.CC_APPROVED, statement.getStatementId());
  }

  /**
   * Отправляет уведомление о создании документа и кодировании документа для заявки.
   *
   * @param statement Заявка клиента, для которой отправляется уведомление.
   */
  public void sendCodeDocument(Statement statement) {
    CreditDto creditDto = creditMapper.toCreditDto(statement.getCredit());

    dealProducer.sendCodeDocumentRequestNotification(
        statement.getClient().getEmail(),
        Theme.CREATED_DOCUMENTS,
        statement.getStatementId(),
        creditDto);
  }

  /**
   * Отправляет уведомление о подписании документа с кодом.
   *
   * @param statement Заявка клиента, для которой отправляется уведомление.
   * @param ses Код подтверждения.
   */
  public void signCodeDocument(Statement statement, String ses) {
    dealProducer.signCodeDocumentRequestNotification(
        statement.getClient().getEmail(), Theme.SIGN_DOCUMENTS, statement.getStatementId(), ses);
  }

  /**
   * Отправляет уведомление о том, что кредит выдан.
   *
   * @param statement Заявка клиента, для которой отправляется уведомление.
   */
  public void codeDocument(Statement statement) {
    dealProducer.codeDocumentRequestNotification(
        statement.getClient().getEmail(), Theme.CREDIT_ISSUED, statement.getStatementId());
  }
}
