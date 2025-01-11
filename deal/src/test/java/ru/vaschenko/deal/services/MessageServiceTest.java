package ru.vaschenko.deal.services;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vaschenko.deal.dto.CreditDto;
import ru.vaschenko.deal.mapping.CreditMapper;
import ru.vaschenko.deal.models.Statement;
import ru.vaschenko.deal.models.enams.Theme;
import ru.vaschenko.deal.services.kafka.producer.KafkaDealProducer;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

  @Mock private CreditMapper creditMapper;

  @Mock private KafkaDealProducer dealProducer;

  @InjectMocks private MessageService messageService;

  private EasyRandom easyRandom;
  private Statement statement;

  @BeforeEach
  void setUp() {
    easyRandom = new EasyRandom();

    // Given
    statement = easyRandom.nextObject(Statement.class);
    statement.getClient().setEmail("test@example.com");
    statement.setStatementId(UUID.randomUUID());
  }

  @Test
  void finishRegistration_ShouldSendNotification() {
    // When
    messageService.finishRegistration(statement);

    // Then
    verify(dealProducer, times(1))
        .sendFinishRegistrationRequestNotification(
            eq("test@example.com"), eq(Theme.FINISH_REGISTRATION), eq(statement.getStatementId()));

  }

  @Test
  void creditDenied_ShouldSendNotification() {
    // When
    messageService.creditDenied(statement);

    // Then
    verify(dealProducer, times(1))
        .sendCreateDocumentRequestNotification(
            eq("test@example.com"), eq(Theme.CC_DENIED), eq(statement.getStatementId()));
  }

  @Test
  void createDocument_ShouldSendNotification() {
    // When
    messageService.createDocument(statement);

    // Then
    verify(dealProducer, times(1))
        .sendCreateDocumentRequestNotification(
            eq("test@example.com"), eq(Theme.CC_APPROVED), eq(statement.getStatementId()));
  }

  @Test
  void sendCodeDocument_ShouldSendNotificationWithCreditDto() {
    // Given
    CreditDto creditDto = easyRandom.nextObject(CreditDto.class);
    when(creditMapper.toCreditDto(statement.getCredit())).thenReturn(creditDto);

    // When
    messageService.sendCodeDocument(statement);

    // Then
    verify(dealProducer, times(1))
        .sendCodeDocumentRequestNotification(
            eq("test@example.com"),
            eq(Theme.CREATED_DOCUMENTS),
            eq(statement.getStatementId()),
            eq(creditDto));
  }

  @Test
  void signCodeDocument_ShouldSendNotificationWithSes() {
    // When
    messageService.signCodeDocument(statement, "123456");

    // Then
    verify(dealProducer, times(1))
        .signCodeDocumentRequestNotification(
            eq("test@example.com"),
            eq(Theme.SIGN_DOCUMENTS),
            eq(statement.getStatementId()),
            eq("123456"));
  }

  @Test
  void codeDocument_ShouldSendNotification() {
    // When
    messageService.codeDocument(statement);

    // Then
    verify(dealProducer, times(1))
        .codeDocumentRequestNotification(
            eq("test@example.com"), eq(Theme.CREDIT_ISSUED), eq(statement.getStatementId()));
  }
}
