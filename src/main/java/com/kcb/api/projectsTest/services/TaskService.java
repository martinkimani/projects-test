package com.kcb.api.projectsTest.services;

import com.kcb.api.projectsTest.dtos.TaskDto;
import com.kcb.api.projectsTest.dtos.TaskStatus;
import com.kcb.api.projectsTest.entities.Project;
import com.kcb.api.projectsTest.entities.Task;
import com.kcb.api.projectsTest.exceptions.ErrorCode;
import com.kcb.api.projectsTest.exceptions.SystemException;
import com.kcb.api.projectsTest.repositories.ProjectRepository;
import com.kcb.api.projectsTest.repositories.TaskRepository;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 *
 * @author martin
 */
@Service
public class TaskService {
    
    private final TaskRepository taskRepo;
    private final ProjectRepository projectRepo;
    
    public TaskService (TaskRepository taskRepo,ProjectRepository projectRepo) {
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;
    }
    
    public Page<Task> getProjectTasks(long projectId,Map<String,String> pageParams) {
        int pageNumber = Integer.parseInt(pageParams.getOrDefault("pageNo", "1"));
        int pageSize = Integer.parseInt(pageParams.getOrDefault("pageSize", "100"));
        return taskRepo.findByProjectId(projectId,PageRequest.of(pageNumber, pageSize));
    }
    
    public Task addProjectTask(long projectId,TaskDto newtask) {
        Project project = projectRepo.findById(projectId).orElseThrow(() -> new SystemException("project not found", ErrorCode.NOT_CONTENT));
        return taskRepo.save(Task.builder().description(newtask.description()).dueDate(newtask.dueDate())
                .projectId(project).status(TaskStatus.TO_DO).title(newtask.title()).build());
    }
    
    public Task editProjectTask(long taskId,TaskDto editTask) {
        Task oldTask = taskRepo.findById(taskId).orElseThrow(() -> new SystemException("task not found", ErrorCode.NOT_CONTENT));
        oldTask.setDescription(editTask.description());
        oldTask.setDueDate(editTask.dueDate());
        oldTask.setStatus(editTask.status());
        oldTask.setTitle(editTask.title());
        return taskRepo.save(oldTask);
    }
    
    public String deleteTask(long taskId) {
        Task oldTask = taskRepo.findById(taskId).orElseThrow(() -> new SystemException("task not found", ErrorCode.NOT_CONTENT));
        taskRepo.deleteById(oldTask.getId());
        return "Task deleted";
    }
}
