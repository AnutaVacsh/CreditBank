package ru.vaschenko.dossier.dto;

import java.util.UUID;
import lombok.Data;
import ru.vaschenko.dossier.dto.enums.Theme;

@Data
public class EmailMessageCredit {
    private String address;
    private Theme theme;
    private UUID statementId;
    private String text;
    private CreditDto creditDto;
}
