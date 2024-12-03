package com.kcb.api.projectsTest.repositories;

import com.kcb.api.projectsTest.dtos.Summary;
import com.kcb.api.projectsTest.entities.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author martin
 */
public interface ProjectRepository extends JpaRepository<Project, Long>{
    @Query(nativeQuery = true, value = "select p.id,p.name,t.status,count(t.status) as statusCount from projects p inner join tasks t group by t.status")
    List<Summary> getProjectSummary();
}
