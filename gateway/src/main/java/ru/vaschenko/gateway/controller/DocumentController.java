package ru.vaschenko.gateway.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.vaschenko.gateway.api.DocumentApi;
import ru.vaschenko.gateway.client.deal.DealFacade;

@RestController
@RequiredArgsConstructor
public class DocumentController implements DocumentApi {
  private final DealFacade dealClient;

  @Override
  public void sendCodeDocument(UUID statementId) {
    dealClient.sendCodeDocument(statementId);
  }

  @Override
  public void signCodeDocument(UUID statementId) {
    dealClient.signCodeDocument(statementId);
  }

  @Override
  public void codeDocument(UUID statementId, String sesCode) {
    dealClient.codeDocument(statementId, sesCode);
  }
}
