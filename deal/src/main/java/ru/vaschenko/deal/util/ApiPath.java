package ru.vaschenko.deal.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPath {
  public static final String BASE_URL = "/deal";
  public static final String STATEMENT = "/statement";
  public static final String OFFER_SELECT = "/offer/select";
  public static final String CALCULATE_ID = "/calculate/{statementId}";

  public static final String CLIENT_BASE_URL = "/calculator";
  public static final String CLIENT_OFFERS = CLIENT_BASE_URL + "/offers";
  public static final String CLIENT_CALC = CLIENT_BASE_URL + "/calc";
}
