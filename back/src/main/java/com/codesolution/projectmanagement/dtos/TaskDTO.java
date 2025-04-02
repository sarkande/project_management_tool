package com.codesolution.projectmanagement.dtos;

import com.codesolution.projectmanagement.models.Task;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public record TaskDTO(
        Integer id,
        String name,
        String description,
        Integer priority,
        Date dueDate,
        String status,
        String projectName,
        List<UserDTO> users
) {
    public TaskDTO(Task task) {
        this(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getPriority(),
                task.getDueDate(),
                task.getStatus(),
                task.getProject().getName(),
                task.getUsers().stream().map(UserDTO::new).collect(Collectors.toList())
        );
    }
}
