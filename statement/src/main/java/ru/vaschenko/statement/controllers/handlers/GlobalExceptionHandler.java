package ru.vaschenko.statement.controllers.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Обрабатывает исключения типа IllegalArgumentException.
   *
   * @param ex выброшенное исключение.
   * @return ответ с сообщением об ошибке и статусом BAD_REQUEST.
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleScoringCalculationException(Exception ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Обрабатывает исключения типа MethodArgumentNotValidException
   *
   * @param ex выброшенное исключение с ошибками валидации.
   * @return ответ с сообщением об ошибке валидации и статусом BAD_REQUEST.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationException(
      MethodArgumentNotValidException ex) {
    String errorMessage =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

    return buildErrorResponse(errorMessage, HttpStatus.BAD_REQUEST);
  }

  /**
   * Обрабатывает все остальные исключения.
   *
   * @param ex выброшенное исключение.
   * @return ответ с сообщением об ошибке и статусом INTERNAL_SERVER_ERROR.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
    return buildErrorResponse(
        "An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<Map<String, Object>> buildErrorResponse(
      String message, HttpStatus status) {
    Map<String, Object> errorDetails =
        Map.of(
            "timestamp", LocalDateTime.now(),
            "status", status.value(),
            "error", status.getReasonPhrase(),
            "message", message);

    return new ResponseEntity<>(errorDetails, status);
  }
}
