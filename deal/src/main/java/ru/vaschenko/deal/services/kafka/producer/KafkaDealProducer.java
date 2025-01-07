package ru.vaschenko.deal.services.kafka.producer;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.vaschenko.deal.dto.CreditDto;
import ru.vaschenko.deal.dto.EmailMessage;
import ru.vaschenko.deal.dto.EmailMessageCredit;
import ru.vaschenko.deal.models.enams.Theme;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaDealProducer {
  private final KafkaTemplate<String, EmailMessage> kafkaTemplate;

  @Value("${kafka.topics.finish_registration}")
  private String finishRegistration;

  @Value("${kafka.topics.create_documents}")
  private String createDocuments;

  @Value("${kafka.topics.send_documents}")
  private String sendDocuments;

  @Value("${kafka.topics.send_ses}")
  private String sendSesCode;

  @Value("${kafka.topics.credit_issued}")
  private String creditIssued;

  @Value("${kafka.topics.statement_denied}")
  private String statementDenied;

  public void sendFinishRegistrationRequestNotification(
      String email, Theme theme, UUID statementId) {
    sendNotification(email, finishRegistration, theme, statementId);
    log.info("send message in topic finish_registration");
  }

  public void sendCreateDocumentRequestNotification(String email, Theme theme, UUID statementId) {
    if (theme == Theme.CC_APPROVED) {
      sendNotification(email, createDocuments, theme, statementId);
      log.info("send message in topic create_documents");
    } else if (theme == Theme.CC_DENIED) {
      sendNotification(email, statementDenied, theme, statementId);
      log.info("send message in topic statement_denied");
    }
  }

  public void sendCodeDocumentRequestNotification(
      String email, Theme theme, UUID statementId, CreditDto creditDto) {
    sendNotification(email, sendDocuments, theme, statementId, creditDto);
    log.info("send message in topic send_documents");
  }

  public void signCodeDocumentRequestNotification(String email, Theme theme, UUID statementId, String ses) {
    sendNotification(email, sendSesCode, theme, statementId, ses);
    log.info("send message in topic send_ses");
  }

  public void codeDocumentRequestNotification(String email, Theme theme, UUID statementId) {
    sendNotification(email, creditIssued, theme, statementId);
    log.info("send message in topic credit_issued");
  }

  private void sendNotification(String email, String topic, Theme theme, UUID statementId) {
    Message<EmailMessage> message =
        MessageBuilder.withPayload(
                EmailMessage.builder().address(email).theme(theme).statementId(statementId).build())
            .setHeader(KafkaHeaders.TOPIC, topic)
            .build();
    kafkaTemplate.send(message);
  }

  private void sendNotification(
      String email, String topic, Theme theme, UUID statementId, CreditDto creditDto) {
    Message<EmailMessageCredit> message =
        MessageBuilder.withPayload(
                EmailMessageCredit.builder()
                    .address(email)
                    .theme(theme)
                    .statementId(statementId)
                    .creditDto(creditDto)
                    .build())
            .setHeader(KafkaHeaders.TOPIC, topic)
            .build();
    kafkaTemplate.send(message);
  }

  private void sendNotification(
      String email, String topic, Theme theme, UUID statementId, String text) {
    Message<EmailMessage> message =
        MessageBuilder.withPayload(
                EmailMessage.builder()
                    .address(email)
                    .theme(theme)
                    .statementId(statementId)
                    .text(text)
                    .build())
            .setHeader(KafkaHeaders.TOPIC, topic)
            .build();
    kafkaTemplate.send(message);
  }
}
