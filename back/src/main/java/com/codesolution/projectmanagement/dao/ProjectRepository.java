package com.codesolution.projectmanagement.dao;

import com.codesolution.projectmanagement.models.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Integer> {

}
