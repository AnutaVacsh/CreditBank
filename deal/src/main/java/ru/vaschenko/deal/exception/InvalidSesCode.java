package ru.vaschenko.deal.exception;

public class InvalidSesCode extends RuntimeException {
  public InvalidSesCode(String message) {
    super(message);
  }
}
