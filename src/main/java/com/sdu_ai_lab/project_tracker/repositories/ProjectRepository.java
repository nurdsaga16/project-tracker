package com.sdu_ai_lab.project_tracker.repositories;

import com.sdu_ai_lab.project_tracker.entities.Project;
import com.sdu_ai_lab.project_tracker.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("""
    SELECT DISTINCT p FROM Project p
    LEFT JOIN p.tags t
    WHERE
        (:tags IS NULL OR t.id IN :tags)
        AND (:status IS NULL OR p.status = :status)
        AND (:name IS NULL OR UPPER(p.title) LIKE CONCAT('%', UPPER(:name), '%'))
""")
    List<Project> findProjectsByTagsAndStatusAndTitleContaining(List<Integer> tags, ProjectStatus status, String name);
}
