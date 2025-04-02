package com.codesolution.projectmanagement.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserLoginDTOTest {

    @Test
    void testUserLoginDTOInitialization() {
        // Arrange
        String email = "user@example.com";
        String password = "securepassword";

        // Act
        UserLoginDTO userLoginDTO = new UserLoginDTO(email, password);

        // Assert
        assertEquals(email, userLoginDTO.email());
        assertEquals(password, userLoginDTO.password());
    }
}
