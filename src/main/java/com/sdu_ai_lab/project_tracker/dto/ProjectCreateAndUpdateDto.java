package com.sdu_ai_lab.project_tracker.dto;

import com.sdu_ai_lab.project_tracker.enums.ProjectStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record ProjectCreateAndUpdateDto(
        @NotBlank
        String title,
        String description,
        @NotNull
        LocalDate startDate,
        @NotNull
        @FutureOrPresent
        LocalDate endDate,
        List<Long> imageIds,
        @NotNull
        Double progress,
        @NotNull
        ProjectStatus status,
        @NotNull
        Long authorId,
        List<Long> tagIds,           // id существующих тегов
        List<String> newTags,        // имена новых тегов для создания
        List<Long> teamMemberIds     // id участников
) {
}
