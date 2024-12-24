package ru.vaschenko.statement.controllers.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock private BindingResult bindingResult;

    @InjectMocks private GlobalExceptionHandler globalExceptionHandler;

    @Test
    public void testHandleScoringCalculationException() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("Scoring error");

        // When
        ResponseEntity<Map<String, Object>> responseEntity =
                globalExceptionHandler.handleScoringCalculationException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Map<String, Object> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseBody.get("status"));
        assertEquals("Scoring error", responseBody.get("message"));
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), responseBody.get("error"));
    }

    @Test
    public void testHandleMethodArgumentNotValidException() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(null, bindingResult);

        FieldError fieldError = new FieldError("object", "field", "Field is required");
        List<FieldError> fieldErrors = Arrays.asList(fieldError);

        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        // When
        ResponseEntity<Map<String, Object>> responseEntity =
                globalExceptionHandler.handleValidationException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Map<String, Object> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseBody.get("status"));
        assertEquals("field: Field is required", responseBody.get("message"));
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), responseBody.get("error"));
    }

    @Test
    public void testHandleGeneralException() {
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