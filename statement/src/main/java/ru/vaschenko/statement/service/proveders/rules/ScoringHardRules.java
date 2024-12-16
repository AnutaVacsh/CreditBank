package ru.vaschenko.statement.service.proveders.rules;

import ru.vaschenko.statement.dto.ScoringDataDto;
import ru.vaschenko.statement.dto.scoring.RejectionAndMessageScoringDTO;

public interface ScoringHardRules {
    RejectionAndMessageScoringDTO check(ScoringDataDto scoringDataDto);
}
