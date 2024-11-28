package ru.vaschenko.calculator.service.proveders;

import ru.vaschenko.calculator.dto.scoring.PreScoringInfoDTO;
import ru.vaschenko.calculator.dto.scoring.RateAndOtherScoringDto;
import ru.vaschenko.calculator.dto.ScoringDataDto;
import ru.vaschenko.calculator.dto.scoring.RejectionAndMessageScoringDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ScoringProvider {
    RateAndOtherScoringDto fullScoring(ScoringDataDto scoringDataDto);
    RejectionAndMessageScoringDTO hardScoring(ScoringDataDto scoringDataDto);

    RateAndOtherScoringDto softScoring(ScoringDataDto scoringDataDto, BigDecimal rate);

    List<PreScoringInfoDTO> preScoring();

//    List<SimpleScoringInfoDto> simpleScoring();
}
