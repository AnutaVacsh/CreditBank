package ru.vaschenko.deal.services.kafka.producer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import ru.vaschenko.deal.dto.CreditDto;
import ru.vaschenko.deal.dto.EmailMessage;
import ru.vaschenko.deal.models.enams.Theme;

class KafkaDealProducerTest {

  @Mock private KafkaTemplate<String, EmailMessage> kafkaTemplate;

  @InjectMocks private KafkaDealProducer kafkaDealProducer;

  private EasyRandom easyRandom;
  private String email;
  private UUID statementId;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    easyRandom = new EasyRandom();
    email = "test@example.com";
    statementId = UUID.randomUUID();
  }

  @Test
  void sendFinishRegistrationRequestNotification_ShouldSendMessage() {
    // When
    kafkaDealProducer.sendFinishRegistrationRequestNotification(
        email, Theme.FINISH_REGISTRATION, statementId);

    // Then
    verify(kafkaTemplate, times(1)).send(any(Message.class));
  }

  @Test
  void sendCreateDocumentRequestNotification_ShouldSendMessage() {
    // When
    kafkaDealProducer.sendCreateDocumentRequestNotification(email, Theme.CC_APPROVED, statementId);

    // Then
    verify(kafkaTemplate, times(1)).send(any(Message.class));
  }

  @Test
  void sendCodeDocumentRequestNotification_ShouldSendMessageWithCreditDto() {
    // Given
    CreditDto creditDto = easyRandom.nextObject(CreditDto.class);

    // When
    kafkaDealProducer.sendCodeDocumentRequestNotification(
        email, Theme.CREATED_DOCUMENTS, statementId, creditDto);

    // Then
    verify(kafkaTemplate, times(1)).send(any(Message.class));
  }

  @Test
  void signCodeDocumentRequestNotification_ShouldSendMessageWithSes() {
    // Given
    String sesCode = "123456";

    // When
    kafkaDealProducer.signCodeDocumentRequestNotification(
        email, Theme.SIGN_DOCUMENTS, statementId, sesCode);

    // Then
    verify(kafkaTemplate, times(1)).send(any(Message.class));
  }

  @Test
  void codeDocumentRequestNotification_ShouldSendMessage() {
    // When
    kafkaDealProducer.codeDocumentRequestNotification(email, Theme.CREDIT_ISSUED, statementId);

    // Then
    verify(kafkaTemplate, times(1)).send(any(Message.class));
  }
}
