package ru.vaschenko.statement.dto.scoring;

import java.math.BigDecimal;

public record RateAndOtherScoringDto(BigDecimal newRate, BigDecimal otherService) {}
