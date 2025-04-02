package com.codesolution.projectmanagement.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testUserAttributes() {
        // Arrange
        User user = new User();

        // Act
        user.setId(1);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("securepassword");

        // Assert
        assertEquals(1, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("testuser@example.com", user.getEmail());
        assertEquals("securepassword", user.getPassword());
    }

    @Test
    void testUserValidation() {
        // Arrange
        User user = new User();
        user.setUsername(""); // Invalid username
        user.setEmail("invalid-email"); // Invalid email format
        user.setPassword("short"); // Invalid password length

        // Act
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Le nom d'utilisateur est obligatoire")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Format d'email invalide")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Minimum 8 caractères")));
    }

    @Test
    void testUserTasksRelation() {
        // Arrange
        User user = new User();
        Task task1 = new Task();
        Task task2 = new Task();
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);

        // Act
        user.setTasks(tasks);

        // Assert
        assertEquals(2, user.getTasks().size());
        assertTrue(user.getTasks().contains(task1));
        assertTrue(user.getTasks().contains(task2));
    }

    @Test
    void testUserProjectUsersRelation() {
        // Arrange
        User user = new User();
        ProjectUser projectUser1 = new ProjectUser();
        ProjectUser projectUser2 = new ProjectUser();
        List<ProjectUser> projectUsers = new ArrayList<>();
        projectUsers.add(projectUser1);
        projectUsers.add(projectUser2);

        // Act
        user.setProjectUsers(projectUsers);

        // Assert
        assertEquals(2, user.getProjectUsers().size());
        assertTrue(user.getProjectUsers().contains(projectUser1));
        assertTrue(user.getProjectUsers().contains(projectUser2));
    }
}
