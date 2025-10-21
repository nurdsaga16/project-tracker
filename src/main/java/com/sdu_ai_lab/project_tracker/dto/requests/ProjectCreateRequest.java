package com.sdu_ai_lab.project_tracker.dto.requests;

import com.sdu_ai_lab.project_tracker.enums.ProjectVisibility;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectCreateRequest {
    @NotNull
    private Long authorId;
    private String title;
}
