package com.kcb.api.projectsTest.repositories;

import com.kcb.api.projectsTest.entities.Task;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author martin
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByProjectId(long projectId,Pageable page);
}
