package com.codesolution.projectmanagement.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntityAlreadyExistExceptionTest {

    @Test
    void testDefaultConstructor() {
        EntityAlreadyExistException exception = new EntityAlreadyExistException();
        assertEquals("Entity already exist", exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String customMessage = "Custom entity already exists message";
        EntityAlreadyExistException exception = new EntityAlreadyExistException(customMessage);
        assertEquals(customMessage, exception.getMessage());
    }
}
