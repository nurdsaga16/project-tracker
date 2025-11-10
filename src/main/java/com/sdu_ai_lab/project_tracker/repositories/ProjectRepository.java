package com.sdu_ai_lab.project_tracker.repositories;

import com.sdu_ai_lab.project_tracker.entities.Project;
import com.sdu_ai_lab.project_tracker.enums.ProjectStatus;
import com.sdu_ai_lab.project_tracker.enums.ProjectVisibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("""
        SELECT p FROM Project p
        JOIN p.tags t
        WHERE
            (:status IS NULL OR p.status = :status)
            AND (:visibility IS NULL OR p.visibility = :visibility)
            AND (:name IS NULL OR :name = '' OR UPPER(p.title) LIKE CONCAT('%', UPPER(:name), '%'))
            AND (:tags IS NULL OR t.id IN :tags)
        GROUP BY p.id
        HAVING (:tags IS NULL OR COUNT(DISTINCT t.id) = :#{#tags.size()})
    """)
    List<Project> findProjectsByTagsAndStatusAndTitleContaining(
            List<Integer> tags,
            ProjectStatus status,
            String name,
            ProjectVisibility visibility
    );
}
