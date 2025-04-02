package com.codesolution.projectmanagement.dtos;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserProjectsResponseDTOTest {

    @Test
    void testUserProjectsResponseDTOInitialization() {
        // Arrange
        UserDTO userDTO = new UserDTO(1, "user@example.com", "testuser");

        ProjectWithRoleDTO project1 = new ProjectWithRoleDTO(1, "Project A", "Description A", new Date(), "Admin");
        ProjectWithRoleDTO project2 = new ProjectWithRoleDTO(2, "Project B", "Description B", new Date(), "Member");

        List<ProjectWithRoleDTO> projects = List.of(project1, project2);

        // Act
        UserProjectsResponseDTO responseDTO = new UserProjectsResponseDTO(userDTO, projects);

        // Assert
        assertEquals(userDTO, responseDTO.user());
        assertEquals(projects, responseDTO.projects());
        assertEquals(2, responseDTO.projects().size());
        assertEquals("Project A", responseDTO.projects().get(0).name());
        assertEquals("Project B", responseDTO.projects().get(1).name());
    }
}
