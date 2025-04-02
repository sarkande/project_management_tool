package com.codesolution.projectmanagement.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testProjectAttributes() {
        // Arrange
        Project project = new Project();
        Date startDate = new Date();
        project.setId(1);
        project.setName("Project A");
        project.setDescription("Description of Project A");
        project.setStartDate(startDate);

        // Act & Assert
        assertEquals(1, project.getId());
        assertEquals("Project A", project.getName());
        assertEquals("Description of Project A", project.getDescription());
        assertEquals(startDate, project.getStartDate());
    }

    @Test
    void testProjectValidation() {
        // Arrange
        Project project = new Project();
        project.setName(""); // Invalid name
        project.setDescription("Description of Project A");

        // Act
        Set<ConstraintViolation<Project>> violations = validator.validate(project);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Le nom du projet est obligatoire")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("3 à 50 caractères")));
    }

    @Test
    void testAddTasks() {
        // Arrange
        Project project = new Project();
        Task task1 = new Task();
        task1.setName("Task 1");

        // Act
        project.getTasks().add(task1);

        // Assert
        assertEquals(1, project.getTasks().size());
        assertEquals("Task 1", project.getTasks().get(0).getName());
    }

    @Test
    void testSetTasks() {
        // Arrange
        Project project = new Project();
        Task task1 = new Task();
        task1.setName("Task 1");
        Task task2 = new Task();
        task2.setName("Task 2");
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);

        // Act
        project.setTasks(tasks);

        // Assert
        assertEquals(2, project.getTasks().size());
        assertTrue(project.getTasks().contains(task1));
        assertTrue(project.getTasks().contains(task2));
    }
}
