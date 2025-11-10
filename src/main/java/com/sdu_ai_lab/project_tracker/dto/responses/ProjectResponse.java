package com.sdu_ai_lab.project_tracker.dto.responses;

import com.sdu_ai_lab.project_tracker.enums.ProjectStatus;
import com.sdu_ai_lab.project_tracker.enums.ProjectVisibility;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectResponse {
    private Long id;
    private String title;
    private String description;
    private ProjectStatus status;
    private ProjectVisibility visibility;
    private UserResponse author;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> imagePaths;
    private List<UserResponse> teamMembers;
    private List<TagResponse> tags;
}
