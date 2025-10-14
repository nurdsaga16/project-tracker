package com.sdu_ai_lab.project_tracker.services;

import com.sdu_ai_lab.project_tracker.dto.ProjectCreateAndUpdateDto;
import com.sdu_ai_lab.project_tracker.dto.ProjectDto;
import com.sdu_ai_lab.project_tracker.entities.ProjectEntity;
import com.sdu_ai_lab.project_tracker.entities.TagEntity;
import com.sdu_ai_lab.project_tracker.entities.UserEntity;
import com.sdu_ai_lab.project_tracker.mappers.ProjectMapper;
import com.sdu_ai_lab.project_tracker.repositories.ImageRepository;
import com.sdu_ai_lab.project_tracker.repositories.ProjectRepository;
import com.sdu_ai_lab.project_tracker.repositories.TagRepository;
import com.sdu_ai_lab.project_tracker.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private static final Logger log = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public List<ProjectDto> getAllProjects() {
        List<ProjectEntity> projects = projectRepository.findAll();
        return projects.stream().map(projectMapper::toDto).toList();
    }

    public ProjectDto getProjectById(Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow();
        return projectMapper.toDto(project);
    }

    public ProjectDto createProject(ProjectCreateAndUpdateDto projectToCreate) {
        if (!projectToCreate.startDate().isBefore(projectToCreate.endDate())) {
            throw new IllegalArgumentException("Project start date must be 1 day before end date");
        }
        Set<TagEntity> tags = new HashSet<>(tagRepository.findAllById(projectToCreate.tagIds()));
        for (String name : projectToCreate.newTags()) {
            TagEntity tag = new TagEntity();
            tag.setName(name);
            tags.add(tagRepository.save(tag));
        }

        Set<UserEntity> teamMembers = new HashSet<>(userRepository.findAllById(projectToCreate.teamMemberIds()));
        UserEntity author = userRepository.findById(projectToCreate.authorId()).orElseThrow();

        ProjectEntity project = new ProjectEntity();
        project.setTitle(projectToCreate.title());
        project.setDescription(projectToCreate.description());
        project.setStartDate(projectToCreate.startDate());
        project.setEndDate(projectToCreate.endDate());
        project.setStatus(projectToCreate.status());
        project.setProgress(projectToCreate.progress());
        project.setAuthor(author);
        project.setTags(tags);
        project.setTeamMembers(teamMembers);
        if (projectToCreate.imageIds() != null) {
            project.setImages(new HashSet<>(imageRepository.findAllById(projectToCreate.imageIds())));
        }
        
        return projectMapper.toDto(projectRepository.save(project));
    }
    
    public ProjectDto updateProject(
            Long projectId,
            ProjectCreateAndUpdateDto projectToUpdate
    ) {
        var project = projectRepository.findById(projectId).orElseThrow();
        if (!project.getAuthor().getId().equals(projectToUpdate.authorId())) {
            throw new IllegalArgumentException("Project author cannot be changed");
        }
        if (!project.getStartDate().isBefore(project.getEndDate())) {
            throw new IllegalArgumentException("Project start date must be 1 day before end date");
        }
        Set<TagEntity> tags = new HashSet<>(tagRepository.findAllById(projectToUpdate.tagIds()));
        for (String name : projectToUpdate.newTags()) {
            TagEntity tag = new TagEntity();
            tag.setName(name);
            tags.add(tagRepository.save(tag));
        }

        Set<UserEntity> teamMembers = new HashSet<>(userRepository.findAllById(projectToUpdate.teamMemberIds()));
        UserEntity author = userRepository.findById(projectToUpdate.authorId()).orElseThrow();

        project.setTitle(projectToUpdate.title());
        project.setDescription(projectToUpdate.description());
        project.setStartDate(projectToUpdate.startDate());
        project.setEndDate(projectToUpdate.endDate());
        project.setStatus(projectToUpdate.status());
        project.setProgress(projectToUpdate.progress());
        project.setAuthor(author);
        project.setTags(tags);
        project.setTeamMembers(teamMembers);
        if (projectToUpdate.imageIds() != null) {
            project.setImages(new HashSet<>(imageRepository.findAllById(projectToUpdate.imageIds())));
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
