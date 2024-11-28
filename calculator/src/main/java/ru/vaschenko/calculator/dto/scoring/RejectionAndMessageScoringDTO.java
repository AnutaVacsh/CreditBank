package ru.vaschenko.calculator.dto.scoring;

public record RejectionAndMessageScoringDTO(
        boolean rejection,
        String message
) {}
