package com.codesolution.projectmanagement.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntityDontExistExceptionTest {

    @Test
    void testDefaultConstructor() {
        EntityDontExistException exception = new EntityDontExistException();
        assertEquals("Entity not found", exception.getMessage());
    }

    @Test
    void testCustomMessageConstructor() {
        String customMessage = "Custom entity not found message";
        EntityDontExistException exception = new EntityDontExistException(customMessage);
        assertEquals(customMessage, exception.getMessage());
    }
}
