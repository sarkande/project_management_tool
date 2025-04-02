package com.codesolution.projectmanagement.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.codesolution.projectmanagement.exceptions.BadRequestException;
import com.codesolution.projectmanagement.models.Project;
import com.codesolution.projectmanagement.models.ProjectUser;
import com.codesolution.projectmanagement.models.User;
import com.codesolution.projectmanagement.services.MailService;
import com.codesolution.projectmanagement.services.ProjectService;
import com.codesolution.projectmanagement.services.ProjectUserService;
import com.codesolution.projectmanagement.services.UserService;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private UserService userService;

    @MockBean
    private ProjectUserService projectUserService;

    @MockBean
    private MailService mailService;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        //Initialise les mocks
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
    }

    @Test
    void testGetAllProjects() throws Exception {
        //On crée 2 projets, qu'on ajoute à la liste des projets puis on verifie que l'appel à la route /projects retourne bien 2 projets
        Project project1 = new Project();
        Project project2 = new Project();
        when(projectService.findAll()).thenReturn(
            Arrays.asList(project1, project2)
        );

        mockMvc
            .perform(get("/projects"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetProjectById() throws Exception {
        //On crée un projet, on le retourne lors de l'appel à la méthode findById
        //Puis on verifie que l'appel à la route /project/1 retourne bien le projet
        Project project = new Project();
        project.setId(1);
        when(projectService.findById(1)).thenReturn(project);

        mockMvc
            .perform(get("/project/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testCreateProject() throws Exception {
        //On crée un projet, un utilisateur, on retourne l'utilisateur lors de l'appel à la méthode findUserById
        //On retourne l'id du projet lors de l'appel à la méthode created
        //On retourne le projet lors de l'appel à la méthode findById
        //On retourne l'id de la relation lors de l'appel à la méthode created de ProjectUserService

        Project project = new Project();
        project.setId(1);
        User user = new User();
        user.setId(1);

        when(userService.findUserById(1)).thenReturn(user);
        when(projectService.created(any(Project.class))).thenReturn(1);
        when(projectService.findById(1)).thenReturn(project);
        when(projectUserService.created(any(ProjectUser.class))).thenReturn(1);

        mockMvc
            .perform(
                post("/project")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"New Project\"}")
                    .param("userId", "1")
            )
            .andExpect(status().isOk())
            .andExpect(content().string("1"));
    }

    @Test
    void testCreateProjectWithoutUserId() throws Exception {
        //On verifie que l'appel à la route /project sans passer l'id de l'utilisateur retourne une bad request
        mockMvc
            .perform(
                post("/project")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"New Project\"}")
            )
            .andExpect(status().isBadRequest())
            .andExpect(result ->
                assertTrue(
                    result.getResolvedException() instanceof BadRequestException
                )
            )
            .andExpect(result ->
                assertEquals(
                    "userId is required",
                    result.getResolvedException().getMessage()
                )
            );
    }

    @Test
    void testUpdateProject() throws Exception {
        //On crée un projet, on retourne le projet lors de l'appel à la méthode findById
        doNothing().when(projectService).update(anyInt(), any(Project.class));

        mockMvc
            .perform(
                put("/project/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"Updated Project\"}")
            )
            .andExpect(status().isOk());

        verify(projectService, times(1)).update(anyInt(), any(Project.class));
    }

    @Test
    void testUpdatePartialProject() throws Exception {
        //On initialise un projet, on retourne le projet lors de l'appel à la méthode findById
        //Et on verifie que l'appel à la route /project/1 avec un patch retourne bien un status 200
        Project oldProject = new Project();
        oldProject.setId(1);
        oldProject.setName("Old Project");

        Project newProject = new Project();
        newProject.setName("Updated Project");

        when(projectService.findById(1)).thenReturn(oldProject);
        doNothing()
            .when(projectService)
            .updatePartial(eq(1), any(Project.class), any(Project.class));

        // Act
        mockMvc
            .perform(
                patch("/project/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"Updated Project\"}")
            )
            .andExpect(status().isOk());

        // Assert
        verify(projectService, times(1)).updatePartial(
            eq(1),
            eq(oldProject),
            any(Project.class)
        );
    }

    @Test
    void testDeleteProject() throws Exception {
        doNothing().when(projectService).delete(anyInt());

        mockMvc.perform(delete("/project/1")).andExpect(status().isOk());

        verify(projectService, times(1)).delete(anyInt());
    }

    @Test
    void testAddUserToProject() throws Exception {
        User user = new User();
        user.setId(1);
        Project project = new Project();
        project.setId(1);
        User newUser = new User();
        newUser.setId(2);

        when(userService.findUserById(1)).thenReturn(user);
        when(projectService.findById(1)).thenReturn(project);
        when(userService.findByEmail("newuser@example.com")).thenReturn(
            newUser
        );
        when(projectUserService.findUserWithRoleByProjectId(1, 1)).thenReturn(
            "Administrateur"
        );
        when(projectUserService.created(any(ProjectUser.class))).thenReturn(1);

        mockMvc
            .perform(
                post("/project/1/adduser")
                    .param("userMail", "newuser@example.com")
                    .param("role", "Membre")
                    .param("currentUser", "1")
            )
            .andExpect(status().isOk())
            .andExpect(content().string("1"));
    }
}
