package com.sdu_ai_lab.project_tracker.services;

import com.sdu_ai_lab.project_tracker.dto.requests.ProjectUpdateRequest;
import com.sdu_ai_lab.project_tracker.dto.responses.ProjectResponse;
import com.sdu_ai_lab.project_tracker.entities.Project;
import com.sdu_ai_lab.project_tracker.entities.Tag;
import com.sdu_ai_lab.project_tracker.entities.User;
import com.sdu_ai_lab.project_tracker.mappers.ProjectMapper;
import com.sdu_ai_lab.project_tracker.repositories.ImageRepository;
import com.sdu_ai_lab.project_tracker.repositories.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public ProjectResponse createProject(ProjectUpdateRequest projectToCreate) {
        log.info("ProjectService.createProject called title={} authorId={}", projectToCreate.getTitle(), projectToCreate.getAuthorId());
        if (!projectToCreate.getStartDate().isBefore(projectToCreate.getEndDate())) {
            throw new IllegalArgumentException("Project start date must be 1 day before end date");
        }
        Set<Tag> tags = tagService.buildTags(projectToCreate.getTagIds() != null ? new java.util.HashSet<>(projectToCreate.getTagIds()) : null, projectToCreate.getNewTags());
        Set<User> teamMembers = userService.getUsersByIds(projectToCreate.getTeamMemberIds() != null ? new java.util.HashSet<>(projectToCreate.getTeamMemberIds()) : null);
        User author = userService.getUserByIdOrThrow(projectToCreate.getAuthorId());

        Project project = new Project();
        project.setTitle(projectToCreate.getTitle());
        project.setDescription(projectToCreate.getDescription());
        project.setStartDate(projectToCreate.getStartDate());
        project.setEndDate(projectToCreate.getEndDate());
        project.setStatus(projectToCreate.getStatus());
        project.setProgress(projectToCreate.getProgress());
        project.setAuthor(author);
        project.setTags(tags);
        project.setTeamMembers(teamMembers);
        if (projectToCreate.getImageIds() != null) {
            var images = new HashSet<>(imageRepository.findAllById(projectToCreate.getImageIds()));
            images.forEach(img -> img.setProject(project));
            project.setImages(images);
        }
        
        return projectMapper.toDto(projectRepository.save(project));
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
