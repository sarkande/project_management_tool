package com.codesolution.projectmanagement.dao;

import com.codesolution.projectmanagement.models.ProjectUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ProjectUserRepository
    extends CrudRepository<ProjectUser, Integer> {
    // Trouver par ID du projet (colonne "project" en base)
    List<ProjectUser> findByProject_Id(Integer projectId);

    // Trouver par ID de l'utilisateur (colonne "user" en base)
    List<ProjectUser> findByUser_Id(Integer userId);

    // Trouver par ID du projet et ID de l'utilisateur
    Optional<ProjectUser> findByProject_IdAndUser_Id(
        Integer projectId,
        Integer userId
    );

    // Trouver par ID de l'utilisateur
    List<ProjectUser> findByUserId(Integer userId);
    // Trouver par ID du projet et ID de l'utilisateur
    boolean existsByProject_IdAndUser_Id(Integer projectId, Integer userId);
}
