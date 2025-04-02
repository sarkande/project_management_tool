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
import com.codesolution.projectmanagement.services.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectUserService projectUserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;


    @Override
    public List<Task> findAllByProjectId(Integer projectId) {
        projectService.findById(projectId);

        List<Task> tasks = new ArrayList<>();
        taskRepository.findByProjectId(projectId).forEach(tasks::add);
        return tasks;
    }

    @Override
    public Task findById(Integer projectId, Integer taskId) {
        projectService.findById(projectId);


        Optional<Task> task = taskRepository.findByProjectIdAndId( projectId,taskId);
        if (!task.isPresent())
            throw new EntityDontExistException();

        return task.get();
    }

    @Override
    public int create(Integer projectId, Task task) {
        Project project = projectService.findById(projectId);  // Récupère le projet pour s'assurer qu'il existe

        // Assurez-vous que le projet existe bien avant de continuer
        if (project == null) {
            throw new EntityNotFoundException("Projet avec l'ID " + projectId + " non trouvé");
        }

        task.setProject(project);  // Associe le projet à la tâche
        return taskRepository.save(task).getId();  // Sauvegarde la tâche avec le projectId correctement défini
    }


    @Override
    public void update(Integer projectId, Integer taskId, Task task) {
        projectService.findById(projectId);

        task.setId(taskId);
        taskRepository.save(task);
    }

    @Override
    public void updatePartial(Integer projectId, Integer taskId, TaskDTO updateDTO) {
        projectService.findById(projectId);
        Comment comment = new Comment();


        Task task = findById(projectId, taskId);

        if (updateDTO.name() != null) {
            task.setName(updateDTO.name());
        }
        if (updateDTO.description() != null) {
            task.setDescription(updateDTO.description());
        }
        if (updateDTO.priority() != null) {
            task.setPriority(updateDTO.priority());
        }
        if (updateDTO.dueDate() != null) {
            task.setDueDate(updateDTO.dueDate());
        }
        if (updateDTO.status() != null) {
            task.setStatus(updateDTO.status());
        }

        comment.setTask(task);
        comment.setContent("Task has been updated");
        comment.setCreatedAt(new Date());
        comment.setCreatedBy("System");

        task.getComments().add(comment);

        taskRepository.save(task);
    }

    @Override
    public void delete(Integer projectId, Integer taskId) {
        projectService.findById(projectId);

        taskRepository.deleteById(taskId);
    }

    @Override
    public int addUserToTask(Integer projectId, Integer taskId, String email) {
        // Verify if the project exists
        Project project = projectService.findById(projectId);
        if (project == null) {
            throw new EntityNotFoundException("Project not found");
        }

        // Verify if the task exists
        Task task = findById(projectId, taskId);
        if (task == null) {
            throw new EntityNotFoundException("Task not found");
        }

        // Verify if the user exists
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        // Verify if the user is already associated with the project
        if (!projectUserService.isUserInProject(projectId, user.getId())) {
            throw new BadRequestException("User is not associated with the project");
        }

        // Verify if the user is already associated with the task
        if (task.getUsers().contains(user)) {
            throw new BadRequestException("User is already associated with the task");
        }

        // Add the user to the task
        task.getUsers().add(user);
        taskRepository.save(task);
        // We need to also add the task to the user
        user.getTasks().add(task);
        userRepository.save(user);

        //We need to email the user to notify him that he has been added to the task

        this.mailService.sendNotification(email, "Vous avez été assigné à la tâche" + task.getName()+ " du projet " + project.getName());

        // We add a comment to the task to notify that the user has been added to the task
        Comment comment = new Comment();

        comment.setTask(task);
        comment.setContent("User " + user.getEmail() + " has been added to the task");
        comment.setCreatedAt(new Date());
        comment.setCreatedBy(user.getEmail());

        task.getComments().add(comment);
        task.getComments().add(comment);
        taskRepository.save(task);

        return task.getId();
    }
}
