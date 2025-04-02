package com.codesolution.projectmanagement.controllers;

import com.codesolution.projectmanagement.dao.ProjectRepository;
import com.codesolution.projectmanagement.dtos.UserWithRoleDTO;
import com.codesolution.projectmanagement.exceptions.EntityDontExistException;
import com.codesolution.projectmanagement.models.Project;
import com.codesolution.projectmanagement.models.User;
import com.codesolution.projectmanagement.services.ProjectService;
import com.codesolution.projectmanagement.services.ProjectUserService;
import com.codesolution.projectmanagement.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectUserControllerTest {

    @InjectMocks
    private ProjectUserController projectUserController;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectService projectService;

    @Mock
    private ProjectUserService projectUserService;

    @Mock
    private UserService userService;

    private Project project;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialisation de l'objet Project utilisé dans les tests
        project = new Project();
        project.setId(1);
        project.setName("Test Project");
    }

    @Test
    void getUsersWithRolesByProject_ShouldReturnListOfUsersWithRoles() {
        // Arrange
        UserWithRoleDTO userWithRoleDTO = new UserWithRoleDTO(1, "test@example.com", "test","Administrateur" );

        when(projectUserService.findUsersWithRolesByProjectId(project.getId())).thenReturn(List.of(userWithRoleDTO));

        // Act
        List<UserWithRoleDTO> users = projectUserController.getUsersWithRolesByProject(project);

        // Assert
        assertNotNull(users);
        assertEquals(1, users.size());
        verify(projectUserService, times(1)).findUsersWithRolesByProjectId(project.getId());
    }

    @Test
    void getUserWithRoleByProject_ShouldReturnRoleForUser() {
        // Arrange
        int userId = 1;
        String expectedRole = "Administrateur";
        Map<String, String> response = new HashMap<>();
        response.put("role", expectedRole);

        when(projectUserService.findUserWithRoleByProjectId(project.getId(), userId)).thenReturn(expectedRole);

        // Act
        ResponseEntity<Map<String, String>> result = projectUserController.getUserWithRoleByProject(project, userId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedRole, result.getBody().get("role"));
        verify(projectUserService, times(1)).findUserWithRoleByProjectId(project.getId(), userId);
    }

    @Test
    void validateProject_ShouldThrowEntityDontExistException_WhenProjectNotFound() {
        // Arrange
        int invalidProjectId = 999;
        when(projectRepository.findById(invalidProjectId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        EntityDontExistException exception = assertThrows(EntityDontExistException.class, () -> {
            projectUserController.validateProject(invalidProjectId);
        });

        assertEquals("Projet non trouvé", exception.getMessage());
    }
}
