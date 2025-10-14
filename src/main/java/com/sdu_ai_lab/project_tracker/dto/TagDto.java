package com.sdu_ai_lab.project_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

public record TagDto(
        @Null
        Long id,
        @NotBlank
        String name
) {
}
