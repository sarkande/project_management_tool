package com.codesolution.projectmanagement.models;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void testCommentAttributes() {
        // Arrange
        Comment comment = new Comment();
        Date now = new Date();
        Task task = new Task();

        // Act
        comment.setId(1);
        comment.setContent("This is a comment.");
        comment.setCreatedAt(now);
        comment.setCreatedBy("user@example.com");
        comment.setTask(task);

        // Assert
        assertEquals(1, comment.getId());
        assertEquals("This is a comment.", comment.getContent());
        assertEquals(now, comment.getCreatedAt());
        assertEquals("user@example.com", comment.getCreatedBy());
        assertEquals(task, comment.getTask());
    }

    @Test
    void testCommentTaskRelation() {
        // Arrange
        Comment comment = new Comment();
        Task task = new Task();

        // Act
        comment.setTask(task);

        // Assert
        assertEquals(task, comment.getTask());
    }
}
