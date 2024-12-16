package ru.vaschenko.statement.service.proveders.rules;

import ru.vaschenko.statement.dto.scoring.RateAndOtherScoringDto;

public interface PreScoringRules {
  RateAndOtherScoringDto check(boolean scoringDataDto);
}
