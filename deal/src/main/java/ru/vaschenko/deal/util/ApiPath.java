package ru.vaschenko.deal.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPath {
  public static final String BASE_URL = "/deal";
  public static final String STATEMENT = BASE_URL + "/statement";
  public static final String OFFER_SELECT = BASE_URL + "/offer/select";
  public static final String CALCULATE_ID = BASE_URL + "/calculate/{statementId}";

  public static final String CLIENT_BASE_URL = "/calculator";
  public static final String CLIENT_OFFERS = CLIENT_BASE_URL + "/offers";
  public static final String CLIENT_CALC = CLIENT_BASE_URL + "/calc";
}
