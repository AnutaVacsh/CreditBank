package ru.vaschenko.deal.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPath {
  public static final String BASE_URL = "/deal";
  public static final String STATEMENT = "/statement";
  public static final String OFFER_SELECT = "/offer/select";
  public static final String CALCULATE_ID = "/calculate/{statementId}";
  public static final String STATEMENT_STATUS = "/admin/statement/{statementId}/status";

  public static final String DOCUMENT = "/document";
  public static final String DOCUMENT_ID = DOCUMENT + "/{statementId}";
  public static final String SEND = DOCUMENT_ID + "/send";
  public static final String SIGN = DOCUMENT_ID + "/sign";
  public static final String CODE = DOCUMENT_ID + "/code";

  public static final String CLIENT_BASE_URL = "/calculator";
  public static final String CLIENT_OFFERS = CLIENT_BASE_URL + "/offers";
  public static final String CLIENT_CALC = CLIENT_BASE_URL + "/calc";

}
