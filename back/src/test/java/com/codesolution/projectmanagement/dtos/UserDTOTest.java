package com.codesolution.projectmanagement.dtos;

import com.codesolution.projectmanagement.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    void testConstructorWithUser() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");

        // Act
        UserDTO userDTO = new UserDTO(user);

        // Assert
        assertEquals(user.getId(), userDTO.id());
        assertEquals(user.getUsername(), userDTO.username());
        assertEquals(user.getEmail(), userDTO.email());
    }
}
