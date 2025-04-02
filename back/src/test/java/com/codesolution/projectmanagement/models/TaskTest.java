package com.codesolution.projectmanagement.models;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTaskAttributes() {
        // Arrange
        Task task = new Task();
        Date dueDate = new Date();
        Project project = new Project();

        // Act
        task.setId(1);
        task.setName("Task A");
        task.setDescription("Description of Task A");
        task.setPriority(5);
        task.setDueDate(dueDate);
        task.setStatus("Open");
        task.setProject(project);

        // Assert
        assertEquals(1, task.getId());
        assertEquals("Task A", task.getName());
        assertEquals("Description of Task A", task.getDescription());
        assertEquals(5, task.getPriority());
        assertEquals(dueDate, task.getDueDate());
        assertEquals("Open", task.getStatus());
        assertEquals(project, task.getProject());
    }

    @Test
    void testTaskUsersRelation() {
        // Arrange
        Task task = new Task();
        User user1 = new User();
        User user2 = new User();
        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);

        // Act
        task.setUsers(users);

        // Assert
        assertEquals(2, task.getUsers().size());
        assertTrue(task.getUsers().contains(user1));
        assertTrue(task.getUsers().contains(user2));
    }

    @Test
    void testTaskCommentsRelation() {
        // Arrange
        Task task = new Task();
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        List<Comment> comments = List.of(comment1, comment2);

        // Act
        task.setComments(comments);

        // Assert
        assertEquals(2, task.getComments().size());
        assertTrue(task.getComments().contains(comment1));
        assertTrue(task.getComments().contains(comment2));
    }
}
