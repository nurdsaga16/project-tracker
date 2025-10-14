package com.sdu_ai_lab.project_tracker.controllers;

import com.sdu_ai_lab.project_tracker.dto.UserDto;
import com.sdu_ai_lab.project_tracker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUsers() {
        logger.info("Getting all users...");
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public UserDto getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        // userDetails.getUsername() — это username текущего пользователя
        return userService.getUserByUsername(userDetails.getUsername());
    }
}
