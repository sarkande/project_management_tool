package com.codesolution.projectmanagement.dtos;

import com.codesolution.projectmanagement.models.Project;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ProjectWithRoleDTOTest {

    @Test
    void testConstructorWithProjectAndRole() {
        // Arrange
        Date startDate = new Date();
        Project project = new Project();
        project.setId(1);
        project.setName("Project A");
        project.setDescription("Description of Project A");
        project.setStartDate(startDate);

        String role = "Admin";

        // Act
        ProjectWithRoleDTO dto = new ProjectWithRoleDTO(project, role);

        // Assert
        assertEquals(project.getId(), dto.id());
        assertEquals(project.getName(), dto.name());
        assertEquals(project.getDescription(), dto.description());
        assertEquals(project.getStartDate(), dto.startDate());
        assertEquals(role, dto.role());
    }
}
