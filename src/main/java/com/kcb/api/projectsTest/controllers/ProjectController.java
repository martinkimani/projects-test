package com.kcb.api.projectsTest.controllers;

import com.kcb.api.projectsTest.dtos.ProjectDto;
import com.kcb.api.projectsTest.dtos.TaskDto;
import com.kcb.api.projectsTest.repositories.ProjectRepository;
import com.kcb.api.projectsTest.services.ProjectService;
import com.kcb.api.projectsTest.services.TaskService;

import jakarta.validation.Valid;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;


/**
 *
 * @author martin
 */
@SuppressWarnings({"rawtypes","unchecked"})

@RestController
public class ProjectController {
    @Autowired
    private  ProjectService projectService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProjectRepository projectRepository;
    
    @PostMapping("/projects")
    public ResponseEntity createNewProject(@RequestBody @Valid ProjectDto newproject) {
        return new ResponseEntity(projectService.createProject(newproject), HttpStatus.CREATED);
    }
    
    @GetMapping("/projects")
    public ResponseEntity getAllProject() {
        return new ResponseEntity(projectService.getAllProjects(), HttpStatus.OK);
    }
    
    
    @GetMapping("/projects/{projectId}")
    public ResponseEntity getProject(@PathVariable long projectId) {
        return new ResponseEntity(projectService.getProject(projectId), HttpStatus.OK);
    }
    
    
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity getProjectTasks(@PathVariable long projectId, @RequestParam Map<String, String> params) {
        return new ResponseEntity(taskService.getProjectTasks(projectId, params), HttpStatus.OK);
    }
    
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity createProjectTask(@PathVariable long projectId, @RequestBody @Valid TaskDto  newTask) {
        return new ResponseEntity(taskService.addProjectTask(projectId, newTask), HttpStatus.OK);
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity editProjectTask(@PathVariable long taskId, @RequestBody @Valid TaskDto  editTask) {
        return new ResponseEntity(taskService.editProjectTask(taskId, editTask), HttpStatus.OK);
    }

    @DeleteMapping("tasks/{taskId}")
    public ResponseEntity deleteTask(@PathVariable long taskId) {
        return new ResponseEntity(taskService.deleteTask(taskId), HttpStatus.OK);
    }
    
    @GetMapping("/projects/summary")
    public ResponseEntity getProjectSummary() {
        return new ResponseEntity(projectRepository.getProjectSummary(), HttpStatus.OK);
    }
}
