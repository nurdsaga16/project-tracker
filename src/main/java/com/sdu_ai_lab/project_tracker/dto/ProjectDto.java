package com.sdu_ai_lab.project_tracker.dto;

import com.sdu_ai_lab.project_tracker.enums.ProjectStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;
import java.util.List;

public record ProjectDto(
        @Null
        Long id,
        @NotBlank
        String title,
        String description,
        @NotNull
        ProjectStatus status,
        @NotNull
        LocalDate startDate,
        @NotNull
        @FutureOrPresent
        LocalDate endDate,
        List<String> imagePaths,
        @NotNull
        Double progress,
        @NotNull
        UserDto author,
        List<UserDto> teamMembers,
        List<TagDto> tags
) {
}
