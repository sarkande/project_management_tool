package com.codesolution.projectmanagement.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserWithRoleDTOTest {

    @Test
    void testUserWithRoleDTOInitialization() {
        // Arrange
        Integer id = 1;
        String email = "user@example.com";
        String username = "testuser";
        String role = "Admin";

        // Act
        UserWithRoleDTO userWithRoleDTO = new UserWithRoleDTO(id, email, username, role);

        // Assert
        assertEquals(id, userWithRoleDTO.id());
        assertEquals(email, userWithRoleDTO.email());
        assertEquals(username, userWithRoleDTO.username());
        assertEquals(role, userWithRoleDTO.role());
    }
}
