package com.codesolution.projectmanagement.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BadLoginExceptionTest {

    @Test
    void testDefaultConstructor() {
        BadLoginException exception = new BadLoginException();
        assertEquals("Erreur dans le l'adresse mail ou le mot de passe.", exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String customMessage = "Custom login error message";
        BadLoginException exception = new BadLoginException(customMessage);
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String customMessage = "Custom login error message";
        Throwable cause = new NullPointerException("Cause of the error");
        BadLoginException exception = new BadLoginException(customMessage, cause);
        assertEquals(customMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
