package ru.vaschenko.dossier.dto;

import ru.vaschenko.dossier.dto.enums.Theme;

import java.util.UUID;

public class EmailMessage {
  String address;
  Theme theme;
  UUID statementId;
  String text;
}
