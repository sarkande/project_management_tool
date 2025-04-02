package com.codesolution.projectmanagement.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionTest {

    @Test
    void testDefaultConstructor() {
        BadRequestException exception = new BadRequestException();
        assertEquals("Erreur dans le contenu de la requête.", exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String customMessage = "Custom bad request error message";
        BadRequestException exception = new BadRequestException(customMessage);
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String customMessage = "Custom bad request error message";
        Throwable cause = new NullPointerException("Cause of the error");
        BadRequestException exception = new BadRequestException(customMessage, cause);
        assertEquals(customMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
