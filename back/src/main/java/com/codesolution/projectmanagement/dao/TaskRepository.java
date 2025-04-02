package com.codesolution.projectmanagement.dao;

import com.codesolution.projectmanagement.models.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task, Integer> {
    List<Task> findByProjectId(Integer projectId);
    Optional<Task> findByProjectIdAndId(Integer projectId, Integer taskId);

}
