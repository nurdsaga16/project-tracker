package com.sdu_ai_lab.project_tracker.controllers;

import com.sdu_ai_lab.project_tracker.entities.User;
import com.sdu_ai_lab.project_tracker.repositories.UserRepository;
import com.sdu_ai_lab.project_tracker.security.JwtUtil;
import com.sdu_ai_lab.project_tracker.security.TokenBlacklist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;
    private final TokenBlacklist tokenBlacklist;

    @PostMapping("/api/v1/auth/signin")
    public String authenticateUser(
            @RequestBody User user
    ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtils.generateToken(userDetails.getUsername());
    }

    @PostMapping("/api/v1/auth/signup")
    public ResponseEntity<Void> registerUser(
            @RequestBody User user
    ) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken!");
        }
        final User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/api/v1/signout")
    public ResponseEntity<String> signOut(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
        }
        String token = authHeader.substring("Bearer ".length());
        try {
            if (!jwtUtils.validateJwtToken(token)) {
                return ResponseEntity.badRequest().body("Invalid token");
            }
            Date exp = jwtUtils.getExpiration(token);
            long expMillis = exp != null ? exp.getTime() : (System.currentTimeMillis() + 3600_000);
            tokenBlacklist.revoke(token, expMillis);
        } catch (Exception e) {
            tokenBlacklist.revoke(token, System.currentTimeMillis() + 3600_000);
        }
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Signed out successfully");
    }
}
