package com.codesolution.projectmanagement.services.impl;

import com.codesolution.projectmanagement.dao.ProjectRepository;
import com.codesolution.projectmanagement.dao.ProjectUserRepository;
import com.codesolution.projectmanagement.dao.UserRepository;
import com.codesolution.projectmanagement.dtos.UserWithRoleDTO;
import com.codesolution.projectmanagement.exceptions.EntityDontExistException;
import com.codesolution.projectmanagement.models.Project;
import com.codesolution.projectmanagement.models.ProjectUser;
import com.codesolution.projectmanagement.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectUserServiceImplTest {

    @Mock
    private ProjectUserRepository projectUserRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectUserServiceImpl projectUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Arrange
        ProjectUser projectUser1 = new ProjectUser();
        ProjectUser projectUser2 = new ProjectUser();
        when(projectUserRepository.findAll()).thenReturn(Arrays.asList(projectUser1, projectUser2));

        // Act
        List<ProjectUser> projectUsers = projectUserService.findAll();

        // Assert
        assertEquals(2, projectUsers.size());
        verify(projectUserRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        ProjectUser projectUser = new ProjectUser();
        projectUser.setId(1);
        when(projectUserRepository.findById(1)).thenReturn(Optional.of(projectUser));

        // Act
        ProjectUser foundProjectUser = projectUserService.findById(1);

        // Assert
        assertEquals(1, foundProjectUser.getId());
        verify(projectUserRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdThrowsException() {
        // Arrange
        when(projectUserRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityDontExistException.class, () -> projectUserService.findById(1));
    }

    @Test
    void testFindUsersWithRolesByProjectId() {
        // Arrange
        ProjectUser projectUser = new ProjectUser();
        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");
        user.setUsername("username");
        projectUser.setUser(user);
        projectUser.setRole("Administrateur");

        when(projectUserRepository.findByProject_Id(1)).thenReturn(Arrays.asList(projectUser));

        // Act
        List<UserWithRoleDTO> userWithRoleDTOs = projectUserService.findUsersWithRolesByProjectId(1);

        // Assert
        assertEquals(1, userWithRoleDTOs.size());
        assertEquals("Administrateur", userWithRoleDTOs.get(0).role());
        verify(projectUserRepository, times(1)).findByProject_Id(1);
    }

    @Test
    void testCreated() {
        // Arrange
        ProjectUser projectUser = new ProjectUser();
        Project project = new Project();
        project.setId(1);
        User user = new User();
        user.setId(1);
        projectUser.setProject(project);
        projectUser.setUser(user);

        // Simuler le comportement du repository pour retourner un ID défini
        ProjectUser savedProjectUser = new ProjectUser();
        savedProjectUser.setId(1); // Assurez-vous que le ProjectUser sauvegardé a un ID

        when(projectRepository.existsById(1)).thenReturn(true);
        when(userRepository.existsById(1)).thenReturn(true);
        when(projectUserRepository.save(any(ProjectUser.class))).thenReturn(savedProjectUser);

        // Act
        int projectUserId = projectUserService.created(projectUser);

        // Assert
        assertEquals(1, projectUserId); // Assurez-vous que l'ID attendu est correct
        verify(projectUserRepository, times(1)).save(projectUser);
    }


    @Test
    void testUpdate() {
        // Arrange
        ProjectUser projectUser = new ProjectUser();
        projectUser.setId(1);
        Project project = new Project();
        project.setId(1);
        User user = new User();
        user.setId(1);
        projectUser.setProject(project);
        projectUser.setUser(user);

        when(projectUserRepository.findById(1)).thenReturn(Optional.of(projectUser));
        when(projectRepository.existsById(1)).thenReturn(true);
        when(userRepository.existsById(1)).thenReturn(true);

        // Act
        projectUserService.update(1, projectUser);

        // Assert
        verify(projectUserRepository, times(1)).save(projectUser);
    }

    @Test
    void testUpdatePartial() {
        // Arrange
        ProjectUser oldProjectUser = new ProjectUser();
        oldProjectUser.setId(1);
        oldProjectUser.setRole("Membre");

        ProjectUser newProjectUser = new ProjectUser();
        newProjectUser.setRole("Administrateur");

        when(projectUserRepository.findById(1)).thenReturn(Optional.of(oldProjectUser));

        // Act
        projectUserService.updatePartial(1, oldProjectUser, newProjectUser);

        // Assert
        verify(projectUserRepository, times(1)).save(oldProjectUser);
        assertEquals("Administrateur", oldProjectUser.getRole());
    }

    @Test
    void testDelete() {
        // Arrange
        ProjectUser projectUser = new ProjectUser();
        projectUser.setId(1);
        when(projectUserRepository.findById(1)).thenReturn(Optional.of(projectUser));

        // Act
        projectUserService.delete(1);

        // Assert
        verify(projectUserRepository, times(1)).deleteById(1);
    }

    @Test
    void testFindUserWithRoleByProjectId() {
        // Arrange
        ProjectUser projectUser = new ProjectUser();
        projectUser.setRole("Administrateur");
        when(projectUserRepository.findByProject_IdAndUser_Id(1, 1)).thenReturn(Optional.of(projectUser));

        // Act
        String role = projectUserService.findUserWithRoleByProjectId(1, 1);

        // Assert
        assertEquals("Administrateur", role);
        verify(projectUserRepository, times(1)).findByProject_IdAndUser_Id(1, 1);
    }

    @Test
    void testFindProjectUsersByUserId() {
        // Arrange
        ProjectUser projectUser = new ProjectUser();
        when(projectUserRepository.findByUserId(1)).thenReturn(Arrays.asList(projectUser));

        // Act
        List<ProjectUser> projectUsers = projectUserService.findProjectUsersByUserId(1);

        // Assert
        assertEquals(1, projectUsers.size());
        verify(projectUserRepository, times(1)).findByUserId(1);
    }

    @Test
    void testIsUserInProject() {
        // Arrange
        when(projectUserRepository.existsByProject_IdAndUser_Id(1, 1)).thenReturn(true);

        // Act
        Boolean isInProject = projectUserService.isUserInProject(1, 1);

        // Assert
        assertTrue(isInProject);
        verify(projectUserRepository, times(1)).existsByProject_IdAndUser_Id(1, 1);
    }
}
