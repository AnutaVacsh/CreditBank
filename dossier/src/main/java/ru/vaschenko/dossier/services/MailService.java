package ru.vaschenko.dossier.services;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.vaschenko.dossier.client.DealFacade;
import ru.vaschenko.dossier.dto.EmailMessage;
import ru.vaschenko.dossier.dto.EmailMessageCredit;
import ru.vaschenko.dossier.dto.enums.ApplicationStatus;
import ru.vaschenko.dossier.util.ApiPath;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
  private final JavaMailSender sender;
  private final PdfGenerator pdfGenerator;
  private final DealFacade client;

  @Value("${spring.mail.username}")
  private String fromAddress;

  @Value("${client.deal.url}")
  private String dealUrl;

  /**
   * Отправка клиенту письма с текстом "Ваша заявка предварительно одобрена, завершите оформление"
   *
   * @param emailMessage
   */
  public void finishRegistrationEmail(EmailMessage emailMessage) {
    sendMessage(
        emailMessage.getAddress(), "Ваша заявка предварительно одобрена, завершите оформление");
  }

  /**
   * В случает отказа клиенту отправляется н апочту письмо с текстом "К сожалению, вам отказано в
   * кредите"
   *
   * @param emailMessage
   */
  public void creditDeniedEmail(EmailMessage emailMessage) {
    sendMessage(emailMessage.getAddress(), "К сожалению, вам отказано в кредите");
  }

  /**
   * После валидации МС Досье отправляет письмо на почту клиенту с одобрением или отказом. Если
   * кредит одобрен, то в письме присутствует ссылка на запрос "Сформировать документы"
   *
   * @param emailMessage
   */
  public void createDocumentEmail(EmailMessage emailMessage) {
    String text = "Кредит одобрен, перейдите к формированию документов ";
    String link =
        UriComponentsBuilder.fromUriString(dealUrl + ApiPath.BASE_URL + ApiPath.DOCUMENT_SEND)
            .buildAndExpand(emailMessage.getStatementId())
            .toUriString();

    String htmlText =
        String.format("<p>%s<br><a href=\"%s\">%s</a></p>", text, link, "Сформировать документы");

    sendMessage(emailMessage.getAddress(), htmlText);
  }

  /**
   * МС Досье отправляет клиенту на почту документы для подписания и ссылку на запрос на согласие с
   * условиями.
   *
   * @param emailMessage
   */
  public void loanDocumentsEmail(EmailMessageCredit emailMessage) throws MessagingException {
    DataSource dataSource = pdfGenerator.generatePdf(emailMessage);
    String link =
        UriComponentsBuilder.fromUriString(dealUrl + ApiPath.BASE_URL + ApiPath.DOCUMENT_SIGN)
            .buildAndExpand(emailMessage.getStatementId())
            .toUriString();

    String linkDenied =
        UriComponentsBuilder.fromUriString(dealUrl + ApiPath.BASE_URL + ApiPath.STATEMENT_STATUS)
            .buildAndExpand(emailMessage.getStatementId())
            .toUriString();

    String htmlText = String.format("<p><a href=\"%s\">%s</a></p>", link, "Подписать");
    String htmlTextDenied = String.format("<p><a href=\"%s\">%s</a></p>", linkDenied, "Отклонить");
    String fullHtmlText = htmlText + htmlTextDenied;

    sendMessage(emailMessage.getAddress(), fullHtmlText, dataSource);

    client.documentCreated(emailMessage.getStatementId(), ApplicationStatus.DOCUMENTS_CREATED);
  }

  /**
   * Если согласился - МС Досье на почту отправляет код и ссылку на подписание документов, куда
   * клиент должен отправить полученный код в МС Сделка.
   *
   * @param emailMessage
   */
  public void signDocumentSesEmail(EmailMessage emailMessage) {
    String link =
        UriComponentsBuilder.fromUriString(
                dealUrl
                    + ApiPath.BASE_URL
                    + ApiPath.DOCUMENT_CODE
                    + "?status="
                    + ApplicationStatus.DOCUMENTS_CREATED)
            .buildAndExpand(emailMessage.getStatementId())
            .toUriString();
    String sesCode = emailMessage.getText();
    String htmlLink =
        String.format(
            "<p>Ваш код подтверждения %s<br><a href=\"%s\">%s</a></p>",
            sesCode, link, "Подтвердить");

    sendMessage(emailMessage.getAddress(), htmlLink);
  }

  /**
   * Сообщение о выдаче кредита
   *
   * @param emailMessage
   */
  public void creditIssued(EmailMessage emailMessage) {
    sendMessage(emailMessage.getAddress(), "Поздравляем, вам выдан кредит!");
  }

  @SneakyThrows
  private void sendMessage(String address, String text) {
    MimeMessage message = sender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setFrom(fromAddress);
    helper.setTo(address);
    helper.setSubject("Уведомление по кредиту");
    helper.setText(text, true);
    //    helper.setText(htmlLink, true);

    logInfo(address, text);

    sender.send(message);
  }

  private void sendMessage(String address, String fullHtmlText, DataSource dataSource)
      throws MessagingException {
    MimeMessage message = sender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setFrom(fromAddress);
    helper.setTo(address);
    helper.setSubject("Уведомление по кредиту");
    helper.addAttachment("credit documents.pdf", dataSource);
    helper.setText(fullHtmlText, true);

    logInfo(address, fullHtmlText);

    sender.send(message);
  }

  private void logInfo(String address, String text) {
    log.info("Send message to the address {} with text '{}'", address, text);
  }
}
