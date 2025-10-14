package com.sdu_ai_lab.project_tracker.dto;

import com.sdu_ai_lab.project_tracker.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public record UserDto(
        @Null
        Long id,
        @NotBlank
        String username,
        @NotNull
        UserRole role
) {
}
