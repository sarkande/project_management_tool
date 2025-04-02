package com.codesolution.projectmanagement.services.impl;

import com.codesolution.projectmanagement.dao.UserRepository;
import com.codesolution.projectmanagement.dtos.UserDTO;
import com.codesolution.projectmanagement.exceptions.BadLoginException;
import com.codesolution.projectmanagement.exceptions.EntityAlreadyExistException;
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

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        when(userRepository.findByEmailAndPassword("test@example.com", "password")).thenReturn(user);

        // Act
        UserDTO userDTO = userService.login("test@example.com", "password");

        // Assert
        assertEquals("test@example.com", userDTO.email());
        assertEquals("testuser", userDTO.username());
    }

    @Test
    void testLoginFailure() {
        // Arrange
        when(userRepository.findByEmailAndPassword("test@example.com", "wrongpassword")).thenReturn(null);

        // Act & Assert
        assertThrows(BadLoginException.class, () -> userService.login("test@example.com", "wrongpassword"));
    }

    @Test
    void testFindAll() {
        // Arrange
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user1@example.com");
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("user2@example.com");
        user2.setUsername("user2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<UserDTO> users = userService.findAll();

        // Assert
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        UserDTO userDTO = userService.findById(1);

        // Assert
        assertEquals("test@example.com", userDTO.email());
        assertEquals("testuser", userDTO.username());
    }

    @Test
    void testFindByIdFailure() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityDontExistException.class, () -> userService.findById(1));
    }

    @Test
    void testFindByEmailSuccess() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        User foundUser = userService.findByEmail("test@example.com");

        // Assert
        assertEquals("test@example.com", foundUser.getEmail());
    }

    @Test
    void testFindByEmailFailure() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityDontExistException.class, () -> userService.findByEmail("test@example.com"));
    }

    @Test
    void testCreateUserSuccess() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        int userId = userService.created(user);

        // Assert
        assertEquals(1, userId);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUserFailure() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(EntityAlreadyExistException.class, () -> userService.created(user));
    }

    @Test
    void testUpdateUser() {
        // Arrange
        User user = new User();
        user.setId(1);

        // Act
        userService.update(1, user);

        // Assert
        verify(userRepository, times(1)).save(user);
        assertEquals(1, user.getId());
    }

    @Test
    void testUpdatePartialUser() {
        // Arrange
        User oldUser = new User();
        oldUser.setId(1);
        oldUser.setUsername("OldUsername");

        User newUser = new User();
        newUser.setUsername("NewUsername");

        when(userRepository.findById(1)).thenReturn(Optional.of(oldUser));

        // Act
        userService.updatePartial(1, oldUser, newUser);

        // Assert
        verify(userRepository, times(1)).save(oldUser);
        assertEquals("NewUsername", oldUser.getUsername());
    }

    @Test
    void testDeleteUser() {
        // Act
        userService.delete(1);

        // Assert
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testFindUserProjects() {
        // Arrange
        User user = new User();
        user.setId(1);

        Project project1 = new Project();
        Project project2 = new Project();

        ProjectUser projectUser1 = new ProjectUser();
        projectUser1.setProject(project1);

        ProjectUser projectUser2 = new ProjectUser();
        projectUser2.setProject(project2);

        user.setProjectUsers(Arrays.asList(projectUser1, projectUser2));

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        List<Project> projects = userService.findUserProjects(1);

        // Assert
        assertEquals(2, projects.size());
        verify(userRepository, times(1)).findById(1);
    }
}
