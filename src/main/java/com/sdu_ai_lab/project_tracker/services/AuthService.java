package com.sdu_ai_lab.project_tracker.services;

import com.sdu_ai_lab.project_tracker.dto.requests.LoginRequest;
import com.sdu_ai_lab.project_tracker.dto.responses.LoginResponse;
import com.sdu_ai_lab.project_tracker.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    public LoginResponse authenticate(LoginRequest input) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.email(), input.password())
        );
        var user = (User) authentication.getPrincipal();
        String token = tokenService.generateToken(authentication);
        return new LoginResponse(token, user.getEmail());
    }
}

