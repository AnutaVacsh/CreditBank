package ru.vaschenko.deal.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vaschenko.deal.dto.StatementDto;
import ru.vaschenko.deal.models.enams.ApplicationStatus;
import ru.vaschenko.deal.util.ApiPath;

@Tag(name = "Admin API")
@RequestMapping(ApiPath.BASE_URL_ADMIN)
public interface AdminApi {
  @PutMapping(ApiPath.STATEMENT_STATUS)
  @Operation(summary = "Update statement status")
  void documentCreated(@PathVariable UUID statementId, @RequestParam ApplicationStatus status);

  @GetMapping(ApiPath.STATEMENT_ID)
  @Operation(summary = "Get statement by id")
  StatementDto getStatementById(@PathVariable UUID statementId);

  @GetMapping(ApiPath.STATEMENTS)
  @Operation(summary = "Get all statements")
  ResponseEntity<List<StatementDto>> getStatements();
}
