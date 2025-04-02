package com.codesolution.projectmanagement.controllers;

import com.codesolution.projectmanagement.dao.ProjectRepository;
import com.codesolution.projectmanagement.dtos.UserWithRoleDTO;
import com.codesolution.projectmanagement.exceptions.EntityDontExistException;
import com.codesolution.projectmanagement.models.Project;
import com.codesolution.projectmanagement.services.ProjectUserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project/{projectId}") // Préfixe pour toutes les routes de ce contrôleur
public class ProjectUserController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectUserService projectUserService;

    @ModelAttribute("project")
    public Project validateProject(@PathVariable Integer projectId) {
        //Cette méthode permet de valider l'existence du projet
        return projectRepository
            .findById(projectId)
            .orElseThrow(() -> new EntityDontExistException("Projet non trouvé")
            );
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserWithRoleDTO> getUsersWithRolesByProject(
        @ModelAttribute("project") Project project
    ) {
        return projectUserService.findUsersWithRolesByProjectId(
            project.getId()
        );
    }

    @GetMapping("/user/{userId}/role")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, String>> getUserWithRoleByProject(
        @ModelAttribute("project") Project project,
        @PathVariable Integer userId
    ) {
        //Cette méthode permet de récupérer le rôle d'un utilisateur dans un projet
        String role = projectUserService.findUserWithRoleByProjectId(
            project.getId(),
            userId
        );
        Map<String, String> response = new HashMap<>();
        response.put("role", role);
        return ResponseEntity.ok(response);
    }
}
