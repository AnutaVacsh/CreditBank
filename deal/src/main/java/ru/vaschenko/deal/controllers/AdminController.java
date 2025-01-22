package ru.vaschenko.deal.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vaschenko.deal.api.AdminApi;
import ru.vaschenko.deal.dto.StatementDto;
import ru.vaschenko.deal.models.Statement;
import ru.vaschenko.deal.models.enams.ApplicationStatus;
import ru.vaschenko.deal.services.AdminService;
import ru.vaschenko.deal.services.StatementService;
import ru.vaschenko.deal.util.ApiPath;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AdminController implements AdminApi{
  private final AdminService adminService;

  @Override
  public void documentCreated(UUID statementId, ApplicationStatus status) {
    adminService.documentCreated(statementId, status);
  }

  @Override
  public StatementDto getStatementById(UUID statementId) {
    return adminService.getStatementById(statementId);
  }

  @Override
  public ResponseEntity<List<StatementDto>> getStatements() {
    return ResponseEntity.ok(adminService.getStatements());
  }
}
