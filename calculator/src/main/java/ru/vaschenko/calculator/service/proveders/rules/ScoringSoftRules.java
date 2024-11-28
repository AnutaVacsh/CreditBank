package ru.vaschenko.calculator.service.proveders.rules;

import ru.vaschenko.calculator.dto.scoring.RateAndOtherScoringDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;

public interface ScoringSoftRules {
    RateAndOtherScoringDto check(ScoringDataDto scoringDataDto);
}
