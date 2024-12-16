package ru.vaschenko.statement.dto.scoring;

import java.util.Map;

public record PreScoringInfoDTO(
        Map<String, Boolean> rules,
        RateAndOtherScoringDto rateAndOtherScoringDto
) {}
