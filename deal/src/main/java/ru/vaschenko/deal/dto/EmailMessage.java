package ru.vaschenko.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.vaschenko.deal.models.enams.Theme;

import java.util.UUID;

@Data
@Builder
@Schema(description = "Message for communication via Kafka")
public class EmailMessage {

    @Schema(description = "Адрес электронной почты получателя", example = "example@example.com")
    private String address;

    @Schema(description = "Тема письма", example = "Registration Confirmation")
    private Theme theme;

    @Schema(description = "Идентификатор заявки", example = "f7a8fbe2-557e-4bb8-aee3-2b29d5e9b8b6")
    private UUID statementId;

    @Schema(description = "Текст сообщения", example = "Your registration was successful.")
    private String text;
}
