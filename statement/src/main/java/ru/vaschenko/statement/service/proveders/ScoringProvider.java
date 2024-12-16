package ru.vaschenko.statement.service.proveders;

import ru.vaschenko.statement.dto.scoring.PreScoringInfoDTO;
import ru.vaschenko.statement.dto.scoring.RateAndOtherScoringDto;
import ru.vaschenko.statement.dto.ScoringDataDto;
import ru.vaschenko.statement.dto.scoring.RejectionAndMessageScoringDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ScoringProvider {
    RateAndOtherScoringDto fullScoring(ScoringDataDto scoringDataDto);
    RejectionAndMessageScoringDTO hardScoring(ScoringDataDto scoringDataDto);

    RateAndOtherScoringDto softScoring(ScoringDataDto scoringDataDto, BigDecimal rate);

    List<PreScoringInfoDTO> preScoring();

//    List<SimpleScoringInfoDto> simpleScoring();
}
