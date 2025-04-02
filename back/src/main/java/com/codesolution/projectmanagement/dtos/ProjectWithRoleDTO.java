package com.codesolution.projectmanagement.dtos;

import com.codesolution.projectmanagement.models.Project;

import java.util.Date;

public record ProjectWithRoleDTO(Integer id, String name, String description, Date startDate, String role) {
    public ProjectWithRoleDTO(Project project, String role) {
        this(project.getId(), project.getName(), project.getDescription(), project.getStartDate(), role);
    }
}