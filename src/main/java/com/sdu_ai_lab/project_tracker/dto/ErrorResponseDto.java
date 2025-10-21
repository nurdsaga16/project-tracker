package com.sdu_ai_lab.project_tracker.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        String detailedMessage,
        LocalDateTime errorTime
) {}
