package com.codesolution.projectmanagement.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProjectNotFoundExceptionTest {

    @Test
    void testDefaultConstructor() {
        ProjectNotFoundException exception = new ProjectNotFoundException();
        assertEquals("Projet associé non trouvé.", exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String customMessage = "Custom project not found message";
        ProjectNotFoundException exception = new ProjectNotFoundException(customMessage);
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String customMessage = "Custom project not found message";
        Throwable cause = new NullPointerException("Cause of the error");
        ProjectNotFoundException exception = new ProjectNotFoundException(customMessage, cause);
        assertEquals(customMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
