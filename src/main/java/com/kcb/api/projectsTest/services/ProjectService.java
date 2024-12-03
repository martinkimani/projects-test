package com.kcb.api.projectsTest.services;

import com.kcb.api.projectsTest.dtos.ProjectDto;
import com.kcb.api.projectsTest.entities.Project;
import com.kcb.api.projectsTest.exceptions.ErrorCode;
import com.kcb.api.projectsTest.exceptions.SystemException;
import com.kcb.api.projectsTest.repositories.ProjectRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 *
 * @author martin
 */
@Service
public class ProjectService {
    private final ProjectRepository projectRepo;
    
    public ProjectService (ProjectRepository projectRepo) {
        this.projectRepo = projectRepo;
    }
    
    public Project createProject(ProjectDto newProject) {
        return projectRepo.save(Project.builder().name(newProject.name()).description(newProject.description()).build());
    }
    
    public List<Project> getAllProjects() {
        List<Project> allProjects = projectRepo.findAll();
        if(allProjects.isEmpty()) throw new SystemException("Projects Not Found", ErrorCode.NOT_FOUND);
        return allProjects;
    }
    
    public Project getProject(long id) {
        return projectRepo.findById(id).orElseThrow(() -> new SystemException("project not found", ErrorCode.NOT_FOUND));
    }
    
    
}
