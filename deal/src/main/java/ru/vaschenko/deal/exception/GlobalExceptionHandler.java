package ru.vaschenko.deal.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.vaschenko.deal.dto.ErrorMessageDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Обрабатывает исключения типа ScoringCalculationException.
   *
   * @param ex выброшенное исключение.
   * @return ответ с сообщением об ошибке и статусом BAD_REQUEST.
   */
  @ExceptionHandler(ScoringCalculationException.class)
  public ResponseEntity<ErrorMessageDto> handleScoringCalculationException(
      ScoringCalculationException ex) {
    ErrorMessageDto errorMessageDto = new ErrorMessageDto(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessageDto);
  }

  /**
   * Обрабатывает исключения типа MethodArgumentNotValidException.
   *
   * @param ex выброшенное исключение.
   * @return ответ с сообщением об ошибке и статусом BAD_REQUEST.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMessageDto> handleValidationException(
      MethodArgumentNotValidException ex) {
    ErrorMessageDto errorMessageDto =
        new ErrorMessageDto(ex.getAllErrors().get(0).getDefaultMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessageDto);
  }

  /**
   * Обрабатывает исключения типа IllegalArgumentException.
   *
   * @param ex выброшенное исключение.
   * @return ответ с сообщением об ошибке и статусом BAD_REQUEST.
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
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
