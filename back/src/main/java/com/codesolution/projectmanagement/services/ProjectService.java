package com.codesolution.projectmanagement.services;

import com.codesolution.projectmanagement.models.Project;

import java.util.List;

public interface ProjectService {
    public List<Project> findAll();
    public Project findById(Integer id);
    public int created(Project Project);
    public void update(Integer id, Project Project);
    public void updatePartial(Integer id, Project oldProject, Project newProject);
    public void delete(Integer id);
}
