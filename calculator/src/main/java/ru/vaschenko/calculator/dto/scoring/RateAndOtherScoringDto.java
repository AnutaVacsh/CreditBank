package ru.vaschenko.calculator.dto.scoring;

import java.math.BigDecimal;

public record RateAndOtherScoringDto(BigDecimal newRate, BigDecimal otherService) {}
