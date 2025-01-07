package ru.vaschenko.services;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeMessage;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import ru.vaschenko.dossier.dto.EmailMessage;
import ru.vaschenko.dossier.dto.EmailMessageCredit;
import ru.vaschenko.dossier.services.MailService;
import ru.vaschenko.dossier.services.PdfGenerator;

@ExtendWith(MockitoExtension.class)
public class MailServiceTest {

  @Mock private JavaMailSender sender;

  @Mock private PdfGenerator pdfGenerator;

  @InjectMocks private MailService mailService;

  private EmailMessage emailMessage;
  private EmailMessageCredit emailMessageCredit;

  @BeforeEach
  public void setUp() {
    emailMessage = new EmailMessage();
    emailMessage.setAddress("test@example.com");
    emailMessage.setStatementId(UUID.randomUUID());

    emailMessageCredit = new EmailMessageCredit();
    emailMessageCredit.setAddress("test@example.com");
    emailMessageCredit.setStatementId(UUID.randomUUID());

    ReflectionTestUtils.setField(mailService, "fromAddress", "test");
  }

  @Test
  public void testFinishRegistrationEmail() {
    // When
    mailService.finishRegistrationEmail(emailMessage);

    // Then
    verify(sender).send(any(SimpleMailMessage.class));
  }

  @Test
  public void testCreditDeniedEmail() {
    // When
    mailService.creditDeniedEmail(emailMessage);

    // Then
    verify(sender).send(any(SimpleMailMessage.class));
  }

  @Test
  public void testCreateDocumentEmail() {
    // When
    mailService.createDocumentEmail(emailMessage);

    // Then
    verify(sender).send(org.mockito.ArgumentMatchers.any(SimpleMailMessage.class));
  }

  @Test
  public void testLoanDocumentsEmail() throws MessagingException {
    // Given
    DataSource mockDataSource = mock(DataSource.class);
    when(pdfGenerator.generatePdf(emailMessageCredit)).thenReturn(mockDataSource);

    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(sender.createMimeMessage()).thenReturn(mimeMessage);

    // When
    mailService.loanDocumentsEmail(emailMessageCredit);

    // Then
    verify(sender, times(1)).send(any(MimeMessage.class));
    verify(mimeMessage, times(1)).setContent(any(Multipart.class));
  }

  @Test
  public void testSignDocumentSesEmail() {
    // When
    mailService.signDocumentSesEmail(emailMessage);

    // Then
    verify(sender, times(1)).send(org.mockito.ArgumentMatchers.any(SimpleMailMessage.class));
  }

  @Test
  public void testCreditIssued() {
    // When
    mailService.creditIssued(emailMessage);

    // Then
    verify(sender).send(org.mockito.ArgumentMatchers.any(SimpleMailMessage.class));
  }
}
