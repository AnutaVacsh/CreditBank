package ru.vaschenko.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error message structure for API responses")
public record ErrorMessageDto(
        @Schema(description = "Error message", example = "Invalid input data") String message
) {}
