package ru.vaschenko.dossier.util;

public class ApiPatch {
    public static final String BASE_URL = "/v1/deal";

    public static final String DOCUMENT = "/document";
    public static final String DOCUMENT_ID = DOCUMENT + "/{statementId}";
    public static final String SEND = DOCUMENT_ID + "/send";
    public static final String SIGN = DOCUMENT_ID + "/sign";
    public static final String CODE = DOCUMENT_ID + "/code";

}
