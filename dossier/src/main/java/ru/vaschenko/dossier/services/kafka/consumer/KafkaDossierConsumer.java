package ru.vaschenko.dossier.services.kafka.consumer;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.vaschenko.dossier.dto.EmailMessage;
import ru.vaschenko.dossier.dto.EmailMessageCredit;
import ru.vaschenko.dossier.services.MailService;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaDossierConsumer {

  private final MailService mailService;

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

  /**
   * Обработка сообщений топиуов finish-registration, create-documents, send-ses, credit-issued,
   * statement-denied
   *
   * @param record
   */
  @KafkaListener(
      groupId = "${spring.kafka.consumer.group-id}",
      topics = {
        "${kafka.topics.finish_registration}",
        "${kafka.topics.create_documents}",
        "${kafka.topics.send_ses}",
        "${kafka.topics.credit_issued}",
        "${kafka.topics.statement_denied}",
        "${kafka.topics.statement_denied}"
      })
  public void handleMessage(ConsumerRecord<String, EmailMessage> record) {
    logInfo(record);
    EmailMessage emailMessage = record.value();
    if (emailMessage != null) {
      String topic = record.topic();

      if (topic.equals(finishRegistration)) {
        mailService.finishRegistrationEmail(emailMessage);
      } else if (topic.equals(createDocuments)) {
        mailService.createDocumentEmail(emailMessage);
      } else if(topic.equals(statementDenied)){
        mailService.creditDeniedEmail(emailMessage);
      } else if (topic.equals(sendSesCode)) {
        mailService.signDocumentSesEmail(emailMessage);
      } else if (topic.equals(creditIssued)) {
        mailService.creditIssued(emailMessage);
      }
    }
  }

  /**
   * Обработка сообщений топика send-documents
   *
   * @param record
   * @throws MessagingException
   */
  @KafkaListener(
      groupId = "${spring.kafka.consumer.group-id}",
      topics = "${kafka.topics.send_documents}")
  public void handleMessageSendDocuments(ConsumerRecord<String, EmailMessageCredit> record)
      throws MessagingException {
    logInfo(record.topic(), record.key(), record.value().toString());

    EmailMessageCredit emailMessage = record.value();
    if (emailMessage != null) {
      mailService.loanDocumentsEmail(emailMessage);
    }
  }

  private void logInfo(ConsumerRecord<String, EmailMessage> record) {
    logInfo(record.topic(), record.key(), record.value().toString());
  }

  private void logInfo(String topic, String key, String value) {
    log.info("Received topic: {}", topic);
    log.info("Received key: {}", key);
    log.info("Received value: {}", value);
  }
}
