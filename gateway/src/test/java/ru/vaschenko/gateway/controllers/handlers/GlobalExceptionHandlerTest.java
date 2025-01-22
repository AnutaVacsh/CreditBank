package ru.vaschenko.gateway.controllers.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.vaschenko.gateway.controller.handlers.GlobalExceptionHandler;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  @InjectMocks private GlobalExceptionHandler globalExceptionHandler;

  @Test
  void testHandleScoringCalculationException_IllegalArgumentException() {
    // Given
    IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

    // When
    ResponseEntity<Map<String, Object>> responseEntity =
        globalExceptionHandler.handleScoringCalculationException(exception);

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    Map<String, Object> responseBody = responseEntity.getBody();
    assertNotNull(responseBody);
    assertEquals(HttpStatus.BAD_REQUEST.value(), responseBody.get("status"));
    assertEquals("Invalid argument", responseBody.get("message"));
    assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), responseBody.get("error"));
  }

  @Test
  void testHandleException() {
    // Given
    Exception exception = new Exception("Unexpected error occurred");

    // When
    ResponseEntity<Map<String, Object>> responseEntity =
        globalExceptionHandler.handleException(exception);

    // Then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    Map<String, Object> responseBody = responseEntity.getBody();
    assertNotNull(responseBody);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseBody.get("status"));
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), responseBody.get("error"));
    assertEquals(
        "An unexpected error occurred: Unexpected error occurred", responseBody.get("message"));
  }
}
