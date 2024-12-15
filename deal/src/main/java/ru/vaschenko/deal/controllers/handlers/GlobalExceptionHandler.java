package ru.vaschenko.deal.controllers.handlers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.vaschenko.deal.exception.PrescoringException;
import ru.vaschenko.deal.exception.ScoringCalculationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Обрабатывает исключения типа ScoringCalculationException, PrescoringException и
   * IllegalArgumentException.
   *
   * @param ex выброшенное исключение.
   * @return ответ с сообщением об ошибке и статусом BAD_REQUEST.
   */
  @ExceptionHandler({
    ScoringCalculationException.class,
    PrescoringException.class,
    IllegalArgumentException.class
  })
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
    Map<String, Object> errorDetails = new HashMap<>();
    errorDetails.put("timestamp", LocalDateTime.now());
    errorDetails.put("status", status.value());
    errorDetails.put("error", status.getReasonPhrase());
    errorDetails.put("message", message);

    return new ResponseEntity<>(errorDetails, status);
  }
}
