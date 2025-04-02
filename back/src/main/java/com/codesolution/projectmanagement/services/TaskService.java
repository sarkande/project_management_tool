package com.codesolution.projectmanagement.services;

import com.codesolution.projectmanagement.dtos.TaskDTO;
import com.codesolution.projectmanagement.models.Task;

import java.util.List;

public interface TaskService {
    List<Task> findAllByProjectId(Integer projectId);
    Task findById(Integer projectId, Integer taskId);
    int create(Integer projectId, Task task);
    void update(Integer projectId, Integer taskId, Task task);
    public void updatePartial(Integer projectId, Integer taskId, TaskDTO updateDTO) ;
    void delete(Integer projectId, Integer taskId);

    int addUserToTask(Integer projectId, Integer taskId, String email);

}
