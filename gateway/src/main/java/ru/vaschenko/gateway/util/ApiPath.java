package ru.vaschenko.gateway.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPath {
    public static final String BASE_URL_STATEMENT = "/v1/statement";
    public static final String STATEMENT_SELECT = "/select";
    public static final String STATEMENT_REGISTRATION_ID = "/registration/{statementId}";

    public static final String BASE_URL_DOCUMENT = "/v1/document";
    public static final String SEND = "/{statementId}";
    public static final String SIGN = "/{statementId}/sign";
    public static final String CODE = "/{statementId}/code";


    public static final String STATEMENT_CLIENT_URL = "/v1/statement";
    public static final String STATEMENT_OFFER = STATEMENT_CLIENT_URL + "/offer";

    public static final String DEAL_CLIENT_URL = "/deal";
    public static final String CALCULATE_ID = DEAL_CLIENT_URL + "/calculate/{statementId}";
    public static final String CALCULATE_ID_SEND = DEAL_CLIENT_URL + "/document" + SEND + "/send";
    public static final String CALCULATE_ID_SIGN = DEAL_CLIENT_URL + "/document" + SIGN;
    public static final String CALCULATE_ID_CODE = DEAL_CLIENT_URL + "/document" + CODE;

}
