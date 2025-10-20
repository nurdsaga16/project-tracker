package com.sdu_ai_lab.project_tracker.services;

import com.sdu_ai_lab.project_tracker.dto.responses.UserResponse;
import com.sdu_ai_lab.project_tracker.entities.User;
import com.sdu_ai_lab.project_tracker.enums.UserPosition;
import com.sdu_ai_lab.project_tracker.mappers.UserMapper;
import com.sdu_ai_lab.project_tracker.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional
    public UserResponse createUser(String fullName,
                                   String username,
                                   String password,
                                   String description,
                                   UserPosition position,
                                   MultipartFile cv,
                                   MultipartFile avatar) throws IOException {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username is already taken");
        }
        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setDescription(description);
        user.setPosition(position);
        if (cv != null && !cv.isEmpty()) {
            String preferredName = username != null ? username : (fullName != null ? fullName : "cv");
            String path = fileStorageService.saveCv(cv, preferredName);
            user.setCvPath(path);
        }
        if (avatar != null && !avatar.isEmpty()) {
            String preferredName = username != null ? username : (fullName != null ? fullName : "avatar");
            String path = fileStorageService.saveAvatar(avatar, preferredName);
            user.setAvatarPath(path);
        }
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Transactional
    public UserResponse updateUser(Long id,
                                   String fullName,
                                   String password,
                                   String description,
                                   UserPosition position,
                                   MultipartFile cv,
                                   MultipartFile avatar) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (fullName != null) user.setFullName(fullName);
        if (password != null && !password.isBlank()) user.setPassword(passwordEncoder.encode(password));
        if (description != null) user.setDescription(description);
        if (position != null) user.setPosition(position);
        if (cv != null && !cv.isEmpty()) {
            String preferredName = user.getUsername() != null ? user.getUsername() : (fullName != null ? fullName : "cv");
            String path = fileStorageService.saveCv(cv, preferredName);
            user.setCvPath(path);
        }
        if (avatar != null && !avatar.isEmpty()) {
            String preferredName = user.getUsername() != null ? user.getUsername() : (fullName != null ? fullName : "avatar");
            String path = fileStorageService.saveAvatar(avatar, preferredName);
            user.setAvatarPath(path);
        }
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UsernameNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}
