package com.codesolution.projectmanagement.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleValidationExceptions() {
        // Mocking FieldError
        FieldError fieldError1 = new FieldError("objectName", "field1", "error message 1");
        FieldError fieldError2 = new FieldError("objectName", "field2", "error message 2");

        // Mocking BindingResult
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(fieldError1);
        fieldErrors.add(fieldError2);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        // Mocking MethodArgumentNotValidException
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        // Test the handler
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("error message 1", response.getBody().get("field1"));
        assertEquals("error message 2", response.getBody().get("field2"));
    }
}
