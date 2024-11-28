package ru.vaschenko.calculator.service.proveders.rules;

import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.scoring.RejectionAndMessageScoringDTO;

public interface ScoringHardRules {
    RejectionAndMessageScoringDTO check(ScoringDataDto scoringDataDto);
}
