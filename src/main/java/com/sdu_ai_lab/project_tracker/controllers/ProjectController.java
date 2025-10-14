package com.sdu_ai_lab.project_tracker.controllers;

import com.sdu_ai_lab.project_tracker.dto.ProjectCreateAndUpdateDto;
import com.sdu_ai_lab.project_tracker.dto.ProjectDto;
import com.sdu_ai_lab.project_tracker.services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    @GetMapping()
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        logger.info("Getting all projects...");
        List<ProjectDto> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(
            @PathVariable("id") Long projectId
    ) {
        logger.info("Getting project with id {}...", projectId);
        ProjectDto project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }

    @PostMapping()
    public ResponseEntity<ProjectDto> createProject(
            @RequestBody @Valid ProjectCreateAndUpdateDto projectToCreate
    ) {
        logger.info("Creating project...");
        ProjectDto savedProject = projectService.createProject(projectToCreate);
        return ResponseEntity.ok(savedProject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable("id") Long projectId,
            @RequestBody @Valid ProjectCreateAndUpdateDto projectToUpdate
    ) {
        logger.info("Updating project...");
        ProjectDto updatedProject = projectService.updateProject(projectId, projectToUpdate);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable("id") Long projectId
    ) {
        logger.info("Deleting project...");
        projectService.deleteProject(projectId);
        return ResponseEntity.ok().build();
    }
}
