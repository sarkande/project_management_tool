package com.codesolution.projectmanagement.services.impl;

import com.codesolution.projectmanagement.dao.TaskRepository;
import com.codesolution.projectmanagement.dao.UserRepository;
import com.codesolution.projectmanagement.dtos.TaskDTO;
import com.codesolution.projectmanagement.exceptions.BadRequestException;
import com.codesolution.projectmanagement.exceptions.EntityDontExistException;
import com.codesolution.projectmanagement.models.Comment;
import com.codesolution.projectmanagement.models.Project;
import com.codesolution.projectmanagement.models.Task;
import com.codesolution.projectmanagement.models.User;
import com.codesolution.projectmanagement.services.MailService;
import com.codesolution.projectmanagement.services.ProjectService;
import com.codesolution.projectmanagement.services.ProjectUserService;
import com.codesolution.projectmanagement.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectService projectService;

    @Mock
    private UserService userService;

    @Mock
    private ProjectUserService projectUserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllByProjectId() {
        Project project = new Project();
        project.setId(1);
        Task task1 = new Task();
        Task task2 = new Task();
        when(projectService.findById(1)).thenReturn(project);
        when(taskRepository.findByProjectId(1)).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.findAllByProjectId(1);

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findByProjectId(1);
    }

    @Test
    void testFindById() {
        Project project = new Project();
        project.setId(1);
        Task task = new Task();
        task.setId(1);
        when(projectService.findById(1)).thenReturn(project);
        when(taskRepository.findByProjectIdAndId(1, 1)).thenReturn(Optional.of(task));

        Task foundTask = taskService.findById(1, 1);

        assertEquals(1, foundTask.getId());
        verify(taskRepository, times(1)).findByProjectIdAndId(1, 1);
    }

    @Test
    void testFindByIdThrowsException() {
        when(taskRepository.findByProjectIdAndId(1, 1)).thenReturn(Optional.empty());

        assertThrows(EntityDontExistException.class, () -> taskService.findById(1, 1));
    }

    @Test
    void testCreate() {
        Project project = new Project();
        project.setId(1);

        Task task = new Task();
        task.setId(1);

        when(projectService.findById(1)).thenReturn(project);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        int taskId = taskService.create(1, task);

        assertEquals(1, taskId);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testCreateThrowsEntityNotFoundException() {
        when(projectService.findById(1)).thenReturn(null);

        Task task = new Task();

        assertThrows(EntityNotFoundException.class, () -> taskService.create(1, task));
    }

    @Test
    void testUpdate() {
        Task task = new Task();
        task.setId(1);
        when(projectService.findById(1)).thenReturn(new Project());

        taskService.update(1, 1, task);

        verify(taskRepository, times(1)).save(task);
        assertEquals(1, task.getId());
    }

    @Test
    void testUpdatePartial() {
        Task task = new Task();
        task.setId(1);
        task.setName("Old Name");
        task.setComments(new ArrayList<>());

        TaskDTO updateDTO = new TaskDTO(1, "New Name", "New Description", 5, new Date(), "In Progress", null, null);
        when(projectService.findById(1)).thenReturn(new Project());
        when(taskRepository.findByProjectIdAndId(1, 1)).thenReturn(Optional.of(task));

        taskService.updatePartial(1, 1, updateDTO);

        verify(taskRepository, times(1)).save(task);
        assertEquals("New Name", task.getName());
        assertEquals("New Description", task.getDescription());
        assertEquals(5, task.getPriority());
        assertEquals("In Progress", task.getStatus());
    }

    @Test
    void testDelete() {
        when(projectService.findById(1)).thenReturn(new Project());

        taskService.delete(1, 1);

        verify(taskRepository, times(1)).deleteById(1);
    }

    @Test
    void testAddUserToTask() {
        Project project = new Project();
        project.setId(1);
        project.setName("Project A");

        Task task = new Task();
        task.setId(1);
        task.setName("Task A");
        task.setComments(new ArrayList<>());

        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");

        when(projectService.findById(1)).thenReturn(project);
        when(taskRepository.findByProjectIdAndId(1, 1)).thenReturn(Optional.of(task));
        when(userService.findByEmail("user@example.com")).thenReturn(user);
        when(projectUserService.isUserInProject(1, 1)).thenReturn(true);

        int taskId = taskService.addUserToTask(1, 1, "user@example.com");

        assertEquals(1, taskId);
        verify(taskRepository, times(2)).save(task);
        verify(userRepository, times(1)).save(user);
        verify(mailService, times(1)).sendNotification(eq("user@example.com"), anyString());
    }

    @Test
    void testAddUserToTaskThrowsEntityDontExistExceptionForTask() {
        Project project = new Project();
        project.setId(1);

        when(projectService.findById(1)).thenReturn(project);
        when(taskRepository.findByProjectIdAndId(1, 1)).thenReturn(Optional.empty());

        assertThrows(EntityDontExistException.class, () -> taskService.addUserToTask(1, 1, "user@example.com"));
    }

    @Test
    void testAddUserToTaskThrowsEntityNotFoundExceptionForUser() {
        Project project = new Project();
        project.setId(1);

        Task task = new Task();
        task.setId(1);

        when(projectService.findById(1)).thenReturn(project);
        when(taskRepository.findByProjectIdAndId(1, 1)).thenReturn(Optional.of(task));
        when(userService.findByEmail("user@example.com")).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> taskService.addUserToTask(1, 1, "user@example.com"));
    }

    @Test
    void testAddUserToTaskThrowsBadRequestExceptionForProject() {
        Project project = new Project();
        project.setId(1);

        Task task = new Task();
        task.setId(1);

        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");

        when(projectService.findById(1)).thenReturn(project);
        when(taskRepository.findByProjectIdAndId(1, 1)).thenReturn(Optional.of(task));
        when(userService.findByEmail("user@example.com")).thenReturn(user);
        when(projectUserService.isUserInProject(1, 1)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> taskService.addUserToTask(1, 1, "user@example.com"));
    }

    @Test
    void testAddUserToTaskThrowsBadRequestExceptionForTask() {
        Project project = new Project();
        project.setId(1);

        Task task = new Task();
        task.setId(1);
        task.setUsers(new HashSet<>());

        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");

        task.getUsers().add(user);

        when(projectService.findById(1)).thenReturn(project);
        when(taskRepository.findByProjectIdAndId(1, 1)).thenReturn(Optional.of(task));
        when(userService.findByEmail("user@example.com")).thenReturn(user);
        when(projectUserService.isUserInProject(1, 1)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> taskService.addUserToTask(1, 1, "user@example.com"));
    }
}
