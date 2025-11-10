package com.sdu_ai_lab.project_tracker.dto.responses;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String position;
    private String description;
    private String avatarPath;
    private String cvPath;
}
