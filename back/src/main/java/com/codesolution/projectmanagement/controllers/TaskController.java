package com.codesolution.projectmanagement.controllers;

import com.codesolution.projectmanagement.dao.ProjectRepository;
import com.codesolution.projectmanagement.dtos.TaskDTO;
import com.codesolution.projectmanagement.exceptions.EntityDontExistException;
import com.codesolution.projectmanagement.models.Project;
import com.codesolution.projectmanagement.models.Task;
import com.codesolution.projectmanagement.services.TaskService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project/{projectId}") // Préfixe pour toutes les routes de ce contrôleur
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectRepository projectRepository;

    @ModelAttribute("project")
    public Project validateProject(@PathVariable Integer projectId) {
        //On vérifie l'existence du projet
        return projectRepository
            .findById(projectId)
            .orElseThrow(() -> new EntityDontExistException("Projet non trouvé")
            );
    }

    // Lister toutes les tâches d'un projet
    @GetMapping("/tasks")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> getAllTasksByProjectId(
        @ModelAttribute("project") Project project
    ) {
        List<Task> tasks = taskService.findAllByProjectId(project.getId());
        return tasks.stream().map(TaskDTO::new).collect(Collectors.toList());
    }

    // Ajouter une tâche à un projet
    @PostMapping("/task")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer addTaskToProject(
        @ModelAttribute("project") Project project,
        @RequestBody Task task
    ) {
        return taskService.create(project.getId(), task);
    }

    // Obtenir une tâche spécifique d'un projet
    @GetMapping("/task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public Task getTaskById(
        @ModelAttribute("project") Project project,
        @PathVariable Integer taskId
    ) {
        return taskService.findById(project.getId(), taskId);
    }

    // Mettre à jour une tâche spécifique d'un projet
    @PutMapping("/task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateTask(
        @ModelAttribute("project") Project project,
        @PathVariable Integer taskId,
        @RequestBody Task task
    ) {
        taskService.update(project.getId(), taskId, task);
    }

    // Mise à jour partielle d'une tâche d'un projet
    @PatchMapping("/task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public void updatePartialTask(
        @ModelAttribute("project") Project project,
        @PathVariable Integer taskId,
        @RequestBody TaskDTO taskDTO
    ) {
        taskService.updatePartial(project.getId(), taskId, taskDTO);
    }

    // Supprimer une tâche d'un projet
    @DeleteMapping("/task/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(
        @ModelAttribute("project") Project project,
        @PathVariable Integer taskId
    ) {
        taskService.delete(project.getId(), taskId);
    }

    //ajouter un utilisateur à une tâche (l'utilisateur doit être déjà associé au projet) en utilisant son email
    @PostMapping("/task/{taskId}/user")
    @ResponseStatus(HttpStatus.CREATED)
    public void addUserToTask(
        @ModelAttribute("project") Project project,
        @PathVariable Integer taskId,
        @RequestParam String email
    ) {
        taskService.addUserToTask(project.getId(), taskId, email);
    }
}
