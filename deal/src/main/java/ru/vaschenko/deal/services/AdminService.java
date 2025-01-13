package ru.vaschenko.deal.services;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vaschenko.deal.dto.StatementDto;
import ru.vaschenko.deal.mapping.StatementMapping;
import ru.vaschenko.deal.models.enams.ApplicationStatus;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final StatementService statementService;
    private final StatementMapping statementMapping;

    public void documentCreated(UUID statementId, ApplicationStatus status) {
        statementService.updateStatus(statementId, status);
    }

    public StatementDto getStatementById(UUID statementId) {
        return statementMapping.toStatementDto(statementService.findStatementById(statementId));
    }

    public List<StatementDto> getStatements() {
        return statementService.getAllStatement()
                .stream()
                .map(statementMapping::toStatementDto)
                .toList();
    }
}
