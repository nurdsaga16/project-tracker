package com.sdu_ai_lab.project_tracker.repositories;

import com.sdu_ai_lab.project_tracker.entities.Project;
import com.sdu_ai_lab.project_tracker.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
