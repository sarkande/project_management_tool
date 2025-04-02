package com.codesolution.projectmanagement.services.impl;

import com.codesolution.projectmanagement.dao.ProjectRepository;
import com.codesolution.projectmanagement.exceptions.EntityDontExistException;
import com.codesolution.projectmanagement.models.Project;
import com.codesolution.projectmanagement.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository ProjectRepository;

    @Override
    public List<Project> findAll() {
        List<Project> Projects = new ArrayList<Project>();
        ProjectRepository.findAll().forEach(Projects::add);
        return Projects;
    }

    @Override
    public Project findById(Integer id) {
        Optional<Project> Project = ProjectRepository.findById(id);

        if(!Project.isPresent())
            throw new EntityDontExistException();

        return Project.get();
    }

    @Override
    public int created(Project Project) {
        return ProjectRepository.save(Project).getId();
    }

    @Override
    public void update(Integer id, Project Project) {
        Project.setId(id);
        ProjectRepository.save(Project);
    }

    @Override
    public void updatePartial(Integer id, Project oldProject, Project newProject) {
        //    private Integer id;
        //    private String name;
        //    private String description;
        //    private Date startDate;

        if(newProject.getName() != null){
            oldProject.setName(newProject.getName());
        }
        if(newProject.getDescription() != null){
            oldProject.setDescription(newProject.getDescription());
        }
        if(newProject.getStartDate() != null){
            oldProject.setStartDate(newProject.getStartDate());
        }


        ProjectRepository.save(oldProject);
    }

    @Override
    public void delete(Integer id) {
        ProjectRepository.deleteById(id);
    }
}
