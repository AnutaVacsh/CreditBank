package ru.vaschenko.gateway.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vaschenko.gateway.util.ApiPath;

@RequestMapping(ApiPath.BASE_URL_DOCUMENT)
@Tag(name = "Document API", description = "API for managing document")
public interface DocumentApi {
  /**
   * Отправка документов.
   *
   * @param statementId идентификатор заявки
   */
  @Operation(
      summary = "Send documents",
      description =
          "Sends a request to initiate the document sending process for the given statement ID.")
  @PostMapping(ApiPath.SEND)
  void sendCodeDocument(@PathVariable UUID statementId);

  /**
   * Подписание документов.
   *
   * @param statementId идентификатор заявки
   */
  @Operation(
      summary = "Sign documents",
      description =
          "Sends a request to initiate the document signing process for the given statement ID.")
  @PostMapping(ApiPath.SIGN)
  void signCodeDocument(@PathVariable UUID statementId);

  /**
   * Подписание документов с использованием кода.
   *
   * @param statementId идентификатор заявки
   * @param sesCode код подтверждения
   */
  @Operation(
      summary = "Finalize document signing",
      description =
          "Completes the document signing process by accepting a confirmation code for the given statement ID.")
  @PostMapping(ApiPath.CODE)
  void codeDocument(@PathVariable UUID statementId, @RequestParam String sesCode);
}
