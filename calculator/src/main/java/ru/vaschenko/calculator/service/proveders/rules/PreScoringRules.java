package ru.vaschenko.calculator.service.proveders.rules;

import ru.vaschenko.calculator.dto.scoring.RateAndOtherScoringDto;

public interface PreScoringRules {
  RateAndOtherScoringDto check(boolean scoringDataDto);
}
