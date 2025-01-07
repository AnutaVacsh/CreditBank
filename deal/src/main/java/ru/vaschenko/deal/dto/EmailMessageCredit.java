package ru.vaschenko.deal.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.vaschenko.deal.models.enams.Theme;

@Data
@Builder
@Schema(description = "Message for communication via Kafka with creditDto")
public class EmailMessageCredit {
    @Schema(description = "Client's email address", example = "example@example.com")
    private String address;

    @Schema(description = "The theme of the message being sent", example = "Credit Approval")
    private Theme theme;

    @Schema(description = "Unique identifier for the statement", example = "f7a8fbe2-557e-4bb8-aee3-2b29d5e9b8b6")
    private UUID statementId;

    @Schema(description = "Content of the message being sent", example = "Your credit has been approved.")
    private String text;

    @Schema(description = "Credit information associated with the message", implementation = CreditDto.class)
    private CreditDto creditDto;
}
