package com.sdu_ai_lab.project_tracker.controllers;

import com.sdu_ai_lab.project_tracker.dto.requests.ProjectCreateRequest;
import com.sdu_ai_lab.project_tracker.dto.requests.ProjectUpdateRequest;
import com.sdu_ai_lab.project_tracker.dto.responses.ProjectResponse;
import com.sdu_ai_lab.project_tracker.enums.ProjectStatus;
import com.sdu_ai_lab.project_tracker.enums.ProjectVisibility;
import com.sdu_ai_lab.project_tracker.services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping()
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        log.info("Getting all projects...");
        List<ProjectResponse> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(
            @PathVariable("id") Long projectId
    ) {
        log.info("Getting project with id {}...", projectId);
        ProjectResponse project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }

    @PostMapping()
    public ResponseEntity<ProjectResponse> createProject(
            @RequestBody @Valid ProjectCreateRequest request
    ) {
        log.info("Creating DRAFT project for author_id={}", request.getAuthorId());
        ProjectResponse draft = projectService.createProject(request);
        return ResponseEntity.ok(draft);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable("id") Long projectId,
            @RequestBody @Valid ProjectUpdateRequest projectToUpdate
    ) {
        log.info("Updating project...");
        ProjectResponse updatedProject = projectService.updateProject(projectId, projectToUpdate);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable("id") Long projectId
    ) {
        log.info("Deleting project...");
        projectService.deleteProject(projectId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProjectResponse>> getProjectsByStatus(
            @RequestParam(required = false) ProjectStatus status,
            @RequestParam(required = false) List<Integer> tags,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) ProjectVisibility visibility
    ){
        log.info("Getting projects by status {}...", status);
        List<ProjectResponse> filteredProjects = projectService.filterProjects(status, tags, text, visibility);
        return ResponseEntity.ok(filteredProjects);
    }

}
