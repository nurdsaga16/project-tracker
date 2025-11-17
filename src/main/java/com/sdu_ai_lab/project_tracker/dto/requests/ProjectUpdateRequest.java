package com.sdu_ai_lab.project_tracker.dto.requests;

import com.sdu_ai_lab.project_tracker.enums.ProjectStatus;
import com.sdu_ai_lab.project_tracker.enums.ProjectVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectUpdateRequest {
    private String title;

    @NotBlank
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Long> imageIds;
    private ProjectStatus status;
    private ProjectVisibility visibility;
    private Long authorId;
    private List<Long> tagIds;
    private List<String> newTags;
    private List<Long> teamMemberIds;
}
