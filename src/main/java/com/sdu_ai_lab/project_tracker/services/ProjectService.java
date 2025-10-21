package com.sdu_ai_lab.project_tracker.services;

import com.sdu_ai_lab.project_tracker.dto.requests.ProjectUpdateRequest;
import com.sdu_ai_lab.project_tracker.dto.responses.ProjectResponse;
import com.sdu_ai_lab.project_tracker.entities.Project;
import com.sdu_ai_lab.project_tracker.entities.Tag;
import com.sdu_ai_lab.project_tracker.entities.User;
import com.sdu_ai_lab.project_tracker.mappers.ProjectMapper;
import com.sdu_ai_lab.project_tracker.repositories.ImageRepository;
import com.sdu_ai_lab.project_tracker.repositories.ProjectRepository;
import com.sdu_ai_lab.project_tracker.repositories.TagRepository;
import com.sdu_ai_lab.project_tracker.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public List<ProjectResponse> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(projectMapper::toDto).toList();
    }

    public ProjectResponse getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        return projectMapper.toDto(project);
    }

    public ProjectResponse createProject(ProjectUpdateRequest projectToCreate) {
        if (!projectToCreate.getStartDate().isBefore(projectToCreate.getEndDate())) {
            throw new IllegalArgumentException("Project start date must be 1 day before end date");
        }
        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(projectToCreate.getTagIds()));
        for (String name : projectToCreate.getNewTags()) {
            Tag tag = new Tag();
            tag.setName(name);
            tags.add(tagRepository.save(tag));
        }

        Set<User> teamMembers = new HashSet<>(userRepository.findAllById(projectToCreate.getTeamMemberIds()));
        User author = userRepository.findById(projectToCreate.getAuthorId()).orElseThrow();

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
        var project = projectRepository.findById(projectId).orElseThrow();
        if (!project.getAuthor().getId().equals(projectToUpdate.getAuthorId())) {
            throw new IllegalArgumentException("Project author cannot be changed");
        }
        if (!project.getStartDate().isBefore(project.getEndDate())) {
            throw new IllegalArgumentException("Project start date must be 1 day before end date");
        }
        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(projectToUpdate.getTagIds()));
        for (String name : projectToUpdate.getNewTags()) {
            Tag tag = new Tag();
            tag.setName(name);
            tags.add(tagRepository.save(tag));
        }

        Set<User> teamMembers = new HashSet<>(userRepository.findAllById(projectToUpdate.getTeamMemberIds()));
        User author = userRepository.findById(projectToUpdate.getAuthorId()).orElseThrow();

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
        if (!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException("Project with id " + projectId + " not found");
        }
        projectRepository.deleteById(projectId);
        log.info("Project with id {} deleted", projectId);
    }
}
