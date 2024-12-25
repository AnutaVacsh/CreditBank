package ru.vaschenko.deal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.vaschenko.deal.models.enams.Theme;

import java.util.UUID;

@Data
@Builder
public class EmailMessage {
    private String address;
    private Theme theme;
    private UUID statementId;
    private String text;
}
