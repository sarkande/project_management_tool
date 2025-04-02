package com.codesolution.projectmanagement.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectUserTest {

    @Test
    void testProjectUserAttributes() {
        // Arrange
        ProjectUser projectUser = new ProjectUser();
        User user = new User();
        Project project = new Project();

        // Act
        projectUser.setId(1);
        projectUser.setUser(user);
        projectUser.setProject(project);
        projectUser.setRole("Membre");

        // Assert
        assertEquals(1, projectUser.getId());
        assertEquals(user, projectUser.getUser());
        assertEquals(project, projectUser.getProject());
        assertEquals("Membre", projectUser.getRole());
    }

    @Test
    void testInvalidRoleThrowsException() {
        // Arrange
        ProjectUser projectUser = new ProjectUser();

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            projectUser.setRole("InvalidRole");
        });

        assertEquals("Le rôle doit être Administrateur, Membre ou Observateur", thrown.getMessage());
    }

    @Test
    void testValidRoles() {
        // Arrange
        ProjectUser projectUser = new ProjectUser();

        // Act & Assert
        assertDoesNotThrow(() -> projectUser.setRole("Administrateur"));
        assertDoesNotThrow(() -> projectUser.setRole("Membre"));
        assertDoesNotThrow(() -> projectUser.setRole("Observateur"));
    }
}
