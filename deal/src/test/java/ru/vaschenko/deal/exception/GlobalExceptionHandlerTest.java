package ru.vaschenko.deal.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.vaschenko.deal.dto.ErrorMessageDto;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  @Mock private BindingResult bindingResult;

  @InjectMocks private GlobalExceptionHandler globalExceptionHandler;

  @Test
  public void testHandleScoringCalculationException() {
    ScoringCalculationException exception =
        new ScoringCalculationException("Scoring calculation failed");

    ResponseEntity<ErrorMessageDto> responseEntity =
        globalExceptionHandler.handleScoringCalculationException(exception);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    ErrorMessageDto responseBody = responseEntity.getBody();
    assertNotNull(responseBody);
    assertEquals("Scoring calculation failed", responseBody.message());
  }

  @Test
  public void testHandleValidationException() {
    FieldError fieldError = new FieldError("objectName", "fieldName", "Validation failed");
    when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

    MethodArgumentNotValidException exception =
        new MethodArgumentNotValidException(null, bindingResult);

    ResponseEntity<ErrorMessageDto> responseEntity =
        globalExceptionHandler.handleValidationException(exception);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    ErrorMessageDto responseBody = responseEntity.getBody();
    assertNotNull(responseBody);
    assertEquals("Validation failed", responseBody.message());
  }

  @Test
  public void testHandleIllegalArgumentException() {
    IllegalArgumentException exception =
        new IllegalArgumentException("Illegal argument exception occurred");

    ResponseEntity<Map<String, Object>> responseEntity =
        globalExceptionHandler.handleIllegalArgumentException(exception);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    Map<String, Object> responseBody = responseEntity.getBody();
    assertNotNull(responseBody);
    assertEquals(HttpStatus.BAD_REQUEST.value(), responseBody.get("status"));
    assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), responseBody.get("error"));
    assertEquals("Illegal argument exception occurred", responseBody.get("message"));
  }

  @Test
  public void testHandleGeneralException() {
    Exception exception = new Exception("Unexpected error occurred");

    ResponseEntity<Map<String, Object>> responseEntity =
        globalExceptionHandler.handleException(exception);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    Map<String, Object> responseBody = responseEntity.getBody();
    assertNotNull(responseBody);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseBody.get("status"));
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), responseBody.get("error"));
    assertEquals(
        "An unexpected error occurred: Unexpected error occurred", responseBody.get("message"));
  }
}
