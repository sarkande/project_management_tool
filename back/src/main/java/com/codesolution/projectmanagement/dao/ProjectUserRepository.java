package com.codesolution.projectmanagement.dao;

import com.codesolution.projectmanagement.models.Project;
import com.codesolution.projectmanagement.models.ProjectUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectUserRepository extends CrudRepository<ProjectUser, Integer> {

    // Trouver par ID du projet (colonne "project" en base)
    List<ProjectUser> findByProject_Id(Integer projectId);

    // Trouver par ID de l'utilisateur (colonne "user" en base)
    List<ProjectUser> findByUser_Id(Integer userId);

    Optional<ProjectUser> findByProject_IdAndUser_Id(Integer projectId, Integer userId);

    List<ProjectUser> findByUserId(Integer userId);

    boolean existsByProject_IdAndUser_Id(Integer projectId, Integer userId);
}
