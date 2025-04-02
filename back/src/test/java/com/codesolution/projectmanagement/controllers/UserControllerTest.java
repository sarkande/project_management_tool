package com.codesolution.projectmanagement.controllers;

import com.codesolution.projectmanagement.dtos.*;
import com.codesolution.projectmanagement.exceptions.EntityDontExistException;
import com.codesolution.projectmanagement.models.Project;
import com.codesolution.projectmanagement.models.ProjectUser;
import com.codesolution.projectmanagement.models.Task;
import com.codesolution.projectmanagement.models.User;
import com.codesolution.projectmanagement.services.ProjectUserService;
import com.codesolution.projectmanagement.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private ProjectUserService projectUserService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        UserDTO user1 = new UserDTO(1, "user1@example.com", "user1");
        UserDTO user2 = new UserDTO(2, "user2@example.com", "user2");

        when(userService.findAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testLogin() throws Exception {
        UserLoginDTO loginDTO = new UserLoginDTO("user@example.com", "password");
        UserDTO userDTO = new UserDTO(1, "user@example.com", "user");

        when(userService.login(loginDTO.email(), loginDTO.password())).thenReturn(userDTO);

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    void testGetUserById() throws Exception {
        UserDTO userDTO = new UserDTO(1, "user@example.com", "user");

        when(userService.findById(1)).thenReturn(userDTO);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testCreateUser() throws Exception {
        User user = new User();
        user.setId(1);

        when(userService.created(any(User.class))).thenReturn(1);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"email\":\"user@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Test
    void testUpdateUser() throws Exception {
        doNothing().when(userService).update(anyInt(), any(User.class));

        mockMvc.perform(put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"updatedUser\",\"email\":\"updated@example.com\",\"password\":\"newpassword\"}"))
                .andExpect(status().isNoContent());  // Changez .isOk() en .isNoContent()
    }


    @Test
    void testUpdatePartialUser() throws Exception {
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername("existingUser");
        existingUser.setEmail("existing@example.com");
        existingUser.setPassword("existingPassword");

        when(userService.findUserById(1)).thenReturn(existingUser);
        doNothing().when(userService).updatePartial(anyInt(), any(User.class), any(User.class));

        mockMvc.perform(patch("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"partialUpdateUser\",\"email\":\"existing@example.com\",\"password\":\"existingPassword\"}"))
                .andExpect(status().isNoContent());
    }


    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).delete(anyInt());

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserTasks() throws Exception {
        UserDTO userDTO = new UserDTO(1, "user@example.com", "user");
        Project project = new Project();
        project.setId(1);
        project.setName("Test Project");

        Task task1 = new Task();
        task1.setId(1);
        task1.setProject(project); // Associe le projet à la tâche

        Task task2 = new Task();
        task2.setId(2);
        task2.setProject(project); // Associe le projet à la tâche

        ProjectUser projectUser = new ProjectUser();
        projectUser.setProject(project);

        when(userService.findById(1)).thenReturn(userDTO);
        when(projectUserService.findProjectUsersByUserId(1)).thenReturn(Arrays.asList(projectUser));

        mockMvc.perform(get("/user/1/tasks"))
                .andExpect(status().isOk());
    }


    @Test
    void testGetUserProjects() throws Exception {
        UserDTO userDTO = new UserDTO(1, "user@example.com", "user");
        Project project1 = new Project();
        project1.setId(1);
        Project project2 = new Project();
        project2.setId(2);

        ProjectUser projectUser1 = new ProjectUser();
        projectUser1.setProject(project1);
        projectUser1.setRole("Administrateur");

        ProjectUser projectUser2 = new ProjectUser();
        projectUser2.setProject(project2);
        projectUser2.setRole("Membre");

        when(userService.findById(1)).thenReturn(userDTO);
        when(projectUserService.findProjectUsersByUserId(1)).thenReturn(Arrays.asList(projectUser1, projectUser2));

        mockMvc.perform(get("/user/1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
