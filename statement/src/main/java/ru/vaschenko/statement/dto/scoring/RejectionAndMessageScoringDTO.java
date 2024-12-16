package ru.vaschenko.statement.dto.scoring;

public record RejectionAndMessageScoringDTO(
        boolean rejection,
        String message
) {}
