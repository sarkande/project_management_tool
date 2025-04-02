package com.codesolution.projectmanagement.controllers;

import com.codesolution.projectmanagement.dao.ProjectRepository;
import com.codesolution.projectmanagement.dtos.TaskDTO;
import com.codesolution.projectmanagement.exceptions.EntityDontExistException;
import com.codesolution.projectmanagement.models.Project;
import com.codesolution.projectmanagement.models.Task;
import com.codesolution.projectmanagement.services.ProjectService;
import com.codesolution.projectmanagement.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @Mock
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    void testGetAllTasksByProjectId() throws Exception {
        Project project = new Project();
        project.setId(1);

        Task task1 = new Task();
        task1.setId(1);
        task1.setProject(project); // Associe le projet à la tâche

        Task task2 = new Task();
        task2.setId(2);
        task2.setProject(project); // Associe le projet à la tâche

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(taskService.findAllByProjectId(1)).thenReturn(Arrays.asList(task1, task2));

        mockMvc.perform(get("/project/1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }


    @Test
    void testAddTaskToProject() throws Exception {
        Project project = new Project();
        project.setId(1);

        Task task = new Task();
        task.setId(1);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(taskService.create(1, task)).thenReturn(1);

        mockMvc.perform(post("/project/1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Task\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetTaskById() throws Exception {
        Project project = new Project();
        project.setId(1);

        Task task = new Task();
        task.setId(1);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(taskService.findById(1, 1)).thenReturn(task);

        mockMvc.perform(get("/project/1/task/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateTask() throws Exception {
        Project project = new Project();
        project.setId(1);

        Task task = new Task();
        task.setId(1);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        doNothing().when(taskService).update(1, 1, task);

        mockMvc.perform(put("/project/1/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Task\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePartialTask() throws Exception {
        Project project = new Project();
        project.setId(1);

        TaskDTO taskDTO = new TaskDTO(1, "Updated Task", null, null, null, null, null, null);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        doNothing().when(taskService).updatePartial(1, 1, taskDTO);

        mockMvc.perform(patch("/project/1/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Task\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteTask() throws Exception {
        Project project = new Project();
        project.setId(1);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        doNothing().when(taskService).delete(1, 1);

        mockMvc.perform(delete("/project/1/task/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAddUserToTask() throws Exception {
        Project project = new Project();
        project.setId(1);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(taskService.addUserToTask(1, 1, "user@example.com")).thenReturn(1); // Simule le retour de l'ID de la tâche

        mockMvc.perform(post("/project/1/task/1/user")
                        .param("email", "user@example.com"))
                .andExpect(status().isCreated());
    }

}
