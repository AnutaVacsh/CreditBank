package ru.vaschenko.deal.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.deal.models.Statement;
import ru.vaschenko.deal.models.enams.Theme;
import ru.vaschenko.deal.queries.producer.KafkaDealProducer;
import ru.vaschenko.deal.repositories.StatementRepositories;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
  StatementRepositories statementRepositories;
  private final KafkaDealProducer dealProducer;

  public void finishRegistration(Statement statement) {
    dealProducer.sendFinishRegistrationRequestNotification(
        statement.getClient().getEmail(), Theme.FINISH_REGISTRATION, statement.getStatementId());
  }

  public void createDocument(Statement statement) {
    dealProducer.sendCreateDocumentRequestNotification(
        statement.getClient().getEmail(), Theme.CREATED_DOCUMENTS, statement.getStatementId());
  }

  // тема ?
  public void sendCodeDocument(Statement statement) {
    dealProducer.sendCodeDocumentRequestNotification(
        statement.getClient().getEmail(), Theme.PREPARE_DOCUMENTS, statement.getStatementId());
    // топик send-documents

    // дальше написано зпрос put от dossier, изменение статуса application и сохранение в бд?
  }

  public void signCodeDocument(Statement statement) {
    dealProducer.signCodeDocumentRequestNotification(
        statement.getClient().getEmail(), Theme.SIGN_DOCUMENTS, statement.getStatementId());
  }

  public void codeDocument(Statement statement) {
    dealProducer.codeDocumentRequestNotification(
        statement.getClient().getEmail(), Theme.CC_APPROVED, statement.getStatementId());
  }
}
