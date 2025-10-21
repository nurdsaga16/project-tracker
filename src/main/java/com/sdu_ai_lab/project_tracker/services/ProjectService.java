package com.sdu_ai_lab.project_tracker.services;

import com.sdu_ai_lab.project_tracker.dto.requests.ProjectCreateRequest;
import com.sdu_ai_lab.project_tracker.dto.requests.ProjectUpdateRequest;
import com.sdu_ai_lab.project_tracker.dto.responses.ProjectResponse;
import com.sdu_ai_lab.project_tracker.entities.Project;
import com.sdu_ai_lab.project_tracker.entities.Tag;
import com.sdu_ai_lab.project_tracker.entities.User;
import com.sdu_ai_lab.project_tracker.enums.ProjectStatus;
import com.sdu_ai_lab.project_tracker.enums.ProjectVisibility;
import com.sdu_ai_lab.project_tracker.mappers.ProjectMapper;
import com.sdu_ai_lab.project_tracker.repositories.ImageRepository;
import com.sdu_ai_lab.project_tracker.repositories.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    private final ImageRepository imageRepository;
    private final TagService tagService;
    private final UserService userService;

    public List<ProjectResponse> getAllProjects() {
        log.info("ProjectService.getAllProjects called");
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(projectMapper::toDto).toList();
    }

    public ProjectResponse getProjectById(Long projectId) {
        log.info("ProjectService.getProjectById called id={}", projectId);
        Project project = projectRepository.findById(projectId).orElseThrow();
        return projectMapper.toDto(project);
    }

    public ProjectResponse createProject(ProjectCreateRequest request) {
        log.info("ProjectService.createProject called authorId={}", request.getAuthorId());
        User author = userService.getUserByIdOrThrow(request.getAuthorId());

        Project project = new Project();
        project.setAuthor(author);
        project.setVisibility(ProjectVisibility.DRAFT);
        project.setStatus(ProjectStatus.PLANNED);
        project.setProgress(0.0);
        project.setTitle(request.getTitle() != null ? request.getTitle() : "Untitled Project");
        project.setDescription("");
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusDays(1));

        log.info("Привет");
        log.info("Пока");

        Project saved = projectRepository.save(project);
        return projectMapper.toDto(saved);
    }

    
    public ProjectResponse updateProject(
            Long projectId,
            ProjectUpdateRequest projectToUpdate
    ) {
        log.info("ProjectService.updateProject called id={} title={} authorId={}", projectId, projectToUpdate.getTitle(), projectToUpdate.getAuthorId());
        var project = projectRepository.findById(projectId).orElseThrow();
        if (!project.getAuthor().getId().equals(projectToUpdate.getAuthorId())) {
            throw new IllegalArgumentException("Project author cannot be changed");
        }
        if (!project.getStartDate().isBefore(project.getEndDate())) {
            throw new IllegalArgumentException("Project start date must be 1 day before end date");
        }
        Set<Tag> tags = tagService.buildTags(projectToUpdate.getTagIds() != null ? new java.util.HashSet<>(projectToUpdate.getTagIds()) : null, projectToUpdate.getNewTags());
        Set<User> teamMembers = userService.getUsersByIds(projectToUpdate.getTeamMemberIds() != null ? new java.util.HashSet<>(projectToUpdate.getTeamMemberIds()) : null);
        User author = userService.getUserByIdOrThrow(projectToUpdate.getAuthorId());

        project.setTitle(projectToUpdate.getTitle());
        project.setDescription(projectToUpdate.getDescription());
        project.setStartDate(projectToUpdate.getStartDate());
        project.setEndDate(projectToUpdate.getEndDate());
        project.setStatus(projectToUpdate.getStatus());
        project.setVisibility(projectToUpdate.getVisibility());
        project.setProgress(projectToUpdate.getProgress());
        project.setAuthor(author);
        project.setTags(tags);
        project.setTeamMembers(teamMembers);
        if (projectToUpdate.getImageIds() != null) {
            var images = new HashSet<>(imageRepository.findAllById(projectToUpdate.getImageIds()));
            images.forEach(img -> img.setProject(project));
            project.setImages(images);
        }
        
        return projectMapper.toDto(projectRepository.save(project));
    }

    public void deleteProject(
            Long projectId
    ) {
        log.info("ProjectService.deleteProject called id={}", projectId);
        if (!projectRepository.existsById(projectId)) {
            throw new jakarta.persistence.EntityNotFoundException("Project with id " + projectId + " not found");
        }
        projectRepository.deleteById(projectId);
        log.info("Project with id {} deleted", projectId);
    }
}
