package com.sdu_ai_lab.project_tracker.dto.responses;

public record LoginResponse(
        String token,
        String email
) {
}
