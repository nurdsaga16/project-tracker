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
    @NotBlank
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    private List<Long> imageIds;
    @NotNull
    private Double progress;
    @NotNull
    private ProjectStatus status;
    @NotNull
    private ProjectVisibility visibility;
    @NotNull
    private Long authorId;
    private List<Long> tagIds;           // id существующих тегов
    private List<String> newTags;        // имена новых тегов для создания
    private List<Long> teamMemberIds;     // id участников
}
