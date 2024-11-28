package ru.vaschenko.calculator.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPath {
  public static final String BASE_URL = "/calculator";
  public static final String CALCULATOR_OFFERS = BASE_URL + "/offers";
  public static final String CALCULATOR_CALC = BASE_URL + "/calc";
}
