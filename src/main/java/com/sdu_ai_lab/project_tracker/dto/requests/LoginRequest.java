package com.sdu_ai_lab.project_tracker.dto.requests;

public record LoginRequest(
        String email,
        String password
) {
}
