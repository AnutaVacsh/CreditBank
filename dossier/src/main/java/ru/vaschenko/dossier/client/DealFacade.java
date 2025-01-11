package ru.vaschenko.dossier.client;

import feign.FeignException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vaschenko.dossier.dto.enums.ApplicationStatus;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealFacade implements DealClient {
  private final DealClient dealClient;

  @Override
  public void documentCreated(UUID statementId, ApplicationStatus status) {
    try {
      log.info("Sending a request to deal/admin/statement/{statementId}/status");
      dealClient.documentCreated(statementId, status);
      log.info("Response from deal service received successfully");
    } catch (FeignException e) {
      log.error("Request failed due to error: {}", e.getMessage());
      throw e;
    }
  }
}
