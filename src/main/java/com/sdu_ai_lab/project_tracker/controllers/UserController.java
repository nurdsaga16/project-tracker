package com.sdu_ai_lab.project_tracker.controllers;

import com.sdu_ai_lab.project_tracker.dto.responses.UserResponse;
import com.sdu_ai_lab.project_tracker.enums.UserPosition;
import com.sdu_ai_lab.project_tracker.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("${api.version}/users")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        var token = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        var email = (String) token.getToken().getClaim("email");
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<UserResponse> createUser(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String description,
            @RequestParam UserPosition position,
            @RequestParam(value = "cv", required = false) MultipartFile cv,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar
    ) throws IOException {
        UserResponse created = userService.createUser(fullName, email, password, description, position, cv, avatar);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping(path = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(required = false) UserPosition position,
            @RequestParam(value = "cv", required = false) MultipartFile cv,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar
    ) throws IOException {
        UserResponse updated = userService.updateUser(id, fullName, email, password, description, position, cv, avatar);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
