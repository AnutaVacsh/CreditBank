package ru.vaschenko.statement.service.proveders.rules;

import ru.vaschenko.statement.dto.scoring.RateAndOtherScoringDto;
import ru.vaschenko.statement.dto.ScoringDataDto;

public interface ScoringSoftRules {
    RateAndOtherScoringDto check(ScoringDataDto scoringDataDto);
}
