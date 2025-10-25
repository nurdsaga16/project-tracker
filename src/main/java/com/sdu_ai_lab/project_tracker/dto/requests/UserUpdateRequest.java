package com.sdu_ai_lab.project_tracker.dto.requests;

import com.sdu_ai_lab.project_tracker.enums.UserPosition;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateRequest {
    @NotBlank
    private String fullName;
    private String description;
    private String password;
    private MultipartFile avatarPath;
    private MultipartFile cvPath;
    private UserPosition position;
}
