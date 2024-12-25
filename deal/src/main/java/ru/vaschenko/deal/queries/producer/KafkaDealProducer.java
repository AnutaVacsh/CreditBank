package ru.vaschenko.deal.queries.producer;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.vaschenko.deal.dto.EmailMessage;
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

  public void sendFinishRegistrationRequestNotification(
      String email, Theme theme, UUID statementId) {
    sendNotification(email, finishRegistration, theme, statementId);
  }

  public void sendCreateDocumentRequestNotification(String email, Theme theme, UUID statementId) {
    sendNotification(email, createDocuments, theme, statementId);
  }

  public void sendCodeDocumentRequestNotification(String email, Theme theme, UUID statementId) {
    log.info("send message in topic send_documents");
    sendNotification(email, sendDocuments, theme, statementId);
  }

  public void signCodeDocumentRequestNotification(String email, Theme theme, UUID statementId){
    sendNotification(email, sendSesCode, theme, statementId);
  }

  public void codeDocumentRequestNotification(String email, Theme theme, UUID statementId) {
    sendNotification(email, creditIssued, theme, statementId);
  }

  private void sendNotification(String email, String topic, Theme theme, UUID statementId) {
    Message<EmailMessage> message =
        MessageBuilder.withPayload(
                EmailMessage.builder().address(email).theme(theme).statementId(statementId).build())
            .setHeader(KafkaHeaders.TOPIC, topic)
            .build();
    kafkaTemplate.send(message);
  }
}
