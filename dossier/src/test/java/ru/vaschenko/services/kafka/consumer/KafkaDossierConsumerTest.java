package ru.vaschenko.services.kafka.consumer;

import static org.mockito.Mockito.verify;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.vaschenko.dossier.dto.EmailMessage;
import ru.vaschenko.dossier.dto.EmailMessageCredit;
import ru.vaschenko.dossier.services.MailService;
import ru.vaschenko.dossier.services.kafka.consumer.KafkaDossierConsumer;

@ExtendWith(MockitoExtension.class)
public class KafkaDossierConsumerTest {

  @Mock private MailService mailService;

  @InjectMocks private KafkaDossierConsumer kafkaDossierConsumer;

  private EmailMessage emailMessage;

  @BeforeEach
  public void setUp() {
    ReflectionTestUtils.setField(kafkaDossierConsumer, "finishRegistration", "finish-registration");
    ReflectionTestUtils.setField(kafkaDossierConsumer, "createDocuments", "create-documents");
    ReflectionTestUtils.setField(kafkaDossierConsumer, "sendDocuments", "send-documents");
    ReflectionTestUtils.setField(kafkaDossierConsumer, "sendSesCode", "send-ses");
    ReflectionTestUtils.setField(kafkaDossierConsumer, "creditIssued", "credit-issued");
    ReflectionTestUtils.setField(kafkaDossierConsumer, "statementDenied", "statement-denied");

    // Given
    emailMessage = new EmailMessage();
  }

  @Test
  public void testHandleMessage_finishRegistration() {
    // Given
    ConsumerRecord<String, EmailMessage> record =
        new ConsumerRecord<>("finish-registration", 0, 0, "key", emailMessage);

    // When
    kafkaDossierConsumer.handleMessage(record);

    // Then
    verify(mailService).finishRegistrationEmail(emailMessage);
  }

  @Test
  public void testHandleMessage_createDocuments() {
    // Given
    ConsumerRecord<String, EmailMessage> record =
        new ConsumerRecord<>("create-documents", 0, 0, "key", emailMessage);

    // When
    kafkaDossierConsumer.handleMessage(record);

    // Then
    verify(mailService).createDocumentEmail(emailMessage);
  }

  @Test
  public void testHandleMessage_sendSesCode() {
    // Given
    ConsumerRecord<String, EmailMessage> record =
        new ConsumerRecord<>("send-ses", 0, 0, "key", emailMessage);

    // When
    kafkaDossierConsumer.handleMessage(record);

    // Then
    verify(mailService).signDocumentSesEmail(emailMessage);
  }

  @Test
  public void testHandleMessage_creditIssued() {
    // Given
    ConsumerRecord<String, EmailMessage> record =
        new ConsumerRecord<>("credit-issued", 0, 0, "key", emailMessage);

    // When
    kafkaDossierConsumer.handleMessage(record);

    // Then
    verify(mailService).creditIssued(emailMessage);
  }

  @Test
  public void testHandleMessage_statementDenied() {
    // Given
    ConsumerRecord<String, EmailMessage> record =
        new ConsumerRecord<>("statement-denied", 0, 0, "key", emailMessage);

    // When
    kafkaDossierConsumer.handleMessage(record);

    // Then
    verify(mailService).creditDeniedEmail(emailMessage);
  }

  @Test
  public void testHandleMessageSendDocuments() throws Exception {
    // Given
    EmailMessageCredit emailMessageCredit =
        new EmailMessageCredit();
    ConsumerRecord<String, EmailMessageCredit> record =
        new ConsumerRecord<>("send-documents", 0, 0, "key", emailMessageCredit);

    // When
    kafkaDossierConsumer.handleMessageSendDocuments(record);

    // Then
    verify(mailService).loanDocumentsEmail(emailMessageCredit);
  }
}
