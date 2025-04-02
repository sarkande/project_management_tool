package com.codesolution.projectmanagement.dtos;

import com.codesolution.projectmanagement.models.Project;
import com.codesolution.projectmanagement.models.Task;
import com.codesolution.projectmanagement.models.User;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskDTOTest {

    @Test
    void testConstructorWithTask() {
        // Arrange
        Date dueDate = new Date();

        Project project = new Project();
        project.setId(1);
        project.setName("Project A");
        project.setDescription("Description of Project A");
        project.setStartDate(new Date());

        User user1 = new User();
        user1.setId(1);
        user1.setUsername("User1");
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("User2");
        user2.setEmail("user2@example.com");

        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);

        Task task = new Task();
        task.setId(1);
        task.setName("Task A");
        task.setDescription("Description of Task A");
        task.setPriority(5);
        task.setDueDate(dueDate);
        task.setStatus("Open");
        task.setProject(project);
        task.setUsers(users);

        // Act
        TaskDTO taskDTO = new TaskDTO(task);

        // Assert
        assertEquals(task.getId(), taskDTO.id());
        assertEquals(task.getName(), taskDTO.name());
        assertEquals(task.getDescription(), taskDTO.description());
        assertEquals(task.getPriority(), taskDTO.priority());
        assertEquals(task.getDueDate(), taskDTO.dueDate());
        assertEquals(task.getStatus(), taskDTO.status());
        assertEquals(task.getProject().getName(), taskDTO.projectName());
        assertEquals(task.getUsers().size(), taskDTO.users().size());

        // Vérifiez que chaque utilisateur dans le DTO est présent dans le Set original
        task.getUsers().forEach(user -> {
            assertTrue(taskDTO.users().stream().anyMatch(userDTO -> userDTO.id().equals(user.getId())));
        });
    }
}
