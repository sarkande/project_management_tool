package com.codesolution.projectmanagement.controllers;

import com.codesolution.projectmanagement.exceptions.BadRequestException;
import com.codesolution.projectmanagement.models.Project;
import com.codesolution.projectmanagement.models.ProjectUser;
import com.codesolution.projectmanagement.models.User;
import com.codesolution.projectmanagement.services.MailService;
import com.codesolution.projectmanagement.services.ProjectService;
import com.codesolution.projectmanagement.services.ProjectUserService;
import com.codesolution.projectmanagement.services.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectUserService projectUserService;

    @Autowired
    private MailService mailService;

    @GetMapping("/projects")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Project> getAllProjects() {
        return projectService.findAll();
    }

    @GetMapping("/project/{id}")
    public Project getProjectById(@PathVariable Integer id) {
        return projectService.findById(id);
    }

    @PostMapping("/project")
    public Integer createProject(
        @RequestBody Project project,
        @RequestParam(required = false) Integer userId
    ) {
        //Pour créer un projet, on doit également passer l'id de l'utilisateur qui a créé le projet
        //On creera le projet et on fera une association entre le projet et l'utilisateur
        //En utilisant ProjectUser (voir la classe ProjectUser)

        //Si on a pas de userId, on va bloquer la création du projet  et retourner une bad request
        if (userId == null) {
            throw new BadRequestException("userId is required");
        }
        //On verifie si l'utilisateur existe
        User user = userService.findUserById(userId);

        //On crée le projet
        Integer projectId = projectService.created(project);
        Project projectCreated = projectService.findById(projectId);

        //On crée l'association entre le projet et l'utilisateur
        ProjectUser projectUser = new ProjectUser();
        projectUser.setProject(projectCreated);
        projectUser.setUser(user);
        projectUser.setRole("Administrateur");

        projectUserService.created(projectUser);

        return projectId;
    }

    @PutMapping("/project/{id}")
    public void updateProject(
        @PathVariable Integer id,
        @RequestBody Project project
    ) {
        //On appelle la méthode findById pour vérifier si l'utilisateur existe,
        // s'il n'existe pas on declenchera automatiquement l'exception EntityDontExistException
        projectService.findById(id);
        projectService.update(id, project);
    }

    @PatchMapping("/project/{id}")
    public void updatePartialProject(
        @PathVariable Integer id,
        @RequestBody Project newProject
    ) {
        //On appelle la méthode findById pour vérifier si l'utilisateur existe,
        // s'il n'existe pas on declenchera automatiquement l'exception EntityDontExistException
        Project oldProject = projectService.findById(id);

        projectService.updatePartial(id, oldProject, newProject);
    }

    @DeleteMapping("/project/{id}")
    public void deleteProject(@PathVariable Integer id) {
        //On appelle la méthode findById pour vérifier si l'utilisateur existe,
        // s'il n'existe pas on declenchera automatiquement l'exception EntityDontExistException
        projectService.findById(id);
        projectService.delete(id);
    }

    @PostMapping("/project/{projectId}/adduser")
    public Integer addUserToProject(
        @PathVariable Integer projectId,
        @RequestParam String userMail,
        @RequestParam String role,
        @RequestParam Integer currentUser
    ) {
        //Cette route à pour but d'ajouter un utilisateur à un projet

        // On verifie sur l'utilisateur actuel existe
        User user = userService.findUserById(currentUser);
        if (user == null) {
            throw new BadRequestException("User not found");
        }

        //On verifie si le projet existe
        Project project = projectService.findById(projectId);
        if (project == null) {
            throw new BadRequestException("Project not found");
        }

        //On verifie si il a les droits d'ajouté quelqu'un sur ce projet
        if (
            !projectUserService
                .findUserWithRoleByProjectId(projectId, currentUser)
                .equals("Administrateur")
        ) {
            throw new BadRequestException(
                "You don't have the rights to add someone to this project"
            );
        }

        //On verifie si le mail est bien rattaché à un utilisateur qui existe et qui n'est pas déja associé à ce projet
        User newUser = userService.findByEmail(userMail);

        if (newUser == null) {
            throw new BadRequestException("User not found");
        }

        if (projectUserService.isUserInProject(projectId, newUser.getId())) {
            throw new BadRequestException("User already in this project");
        }

        //On crée une nouvelle association entre le projet et l'utilisateur et le role
        ProjectUser projectUser = new ProjectUser();
        projectUser.setProject(project);
        projectUser.setUser(newUser);
        projectUser.setRole(role);

        this.mailService.sendNotification(
                user.getEmail(),
                "Vous avez été assigné au projet " + project.getName()
            );

        return projectUserService.created(projectUser);
    }
}
