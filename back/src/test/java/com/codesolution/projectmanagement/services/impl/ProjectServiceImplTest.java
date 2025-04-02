package com.codesolution.projectmanagement.services.impl;

import com.codesolution.projectmanagement.dao.ProjectRepository;
import com.codesolution.projectmanagement.exceptions.EntityDontExistException;
import com.codesolution.projectmanagement.models.Project;
import com.codesolution.projectmanagement.services.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Arrange
        Project project1 = new Project();
        project1.setId(1);
        Project project2 = new Project();
        project2.setId(2);

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        // Act
        List<Project> projects = projectService.findAll();

        // Assert
        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        Project project = new Project();
        project.setId(1);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        // Act
        Project foundProject = projectService.findById(1);

        // Assert
        assertEquals(1, foundProject.getId());
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdThrowsException() {
        // Arrange
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityDontExistException.class, () -> projectService.findById(1));
    }

    @Test
    void testCreate() {
        // Arrange
        Project project = new Project();
        project.setId(1);

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        int projectId = projectService.created(project);

        // Assert
        assertEquals(1, projectId);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void testUpdate() {
        // Arrange
        Project project = new Project();
        project.setId(1);

        // Act
        projectService.update(1, project);

        // Assert
        verify(projectRepository, times(1)).save(project);
        assertEquals(1, project.getId());
    }

    @Test
    void testUpdatePartial() {
        // Arrange
        Project oldProject = new Project();
        oldProject.setId(1);
        oldProject.setName("Old Name");

        Project newProject = new Project();
        newProject.setName("New Name");

        // Act
        projectService.updatePartial(1, oldProject, newProject);

        // Assert
        verify(projectRepository, times(1)).save(oldProject);
        assertEquals("New Name", oldProject.getName());
    }

    @Test
    void testDelete() {
        // Act
        projectService.delete(1);

        // Assert
        verify(projectRepository, times(1)).deleteById(1);
    }
}
