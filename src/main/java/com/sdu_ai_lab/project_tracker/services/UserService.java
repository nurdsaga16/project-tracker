package com.sdu_ai_lab.project_tracker.services;

import com.sdu_ai_lab.project_tracker.dto.responses.UserResponse;
import com.sdu_ai_lab.project_tracker.entities.User;
import com.sdu_ai_lab.project_tracker.enums.UserPosition;
import com.sdu_ai_lab.project_tracker.mappers.UserMapper;
import com.sdu_ai_lab.project_tracker.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("UserService.loadUserByUsername called email={}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    public UserResponse getUserByEmail(String email) {
        log.info("UserService.getUserByEmail called email={}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    public List<UserResponse> getAllUsers() {
        log.info("UserService.getAllUsers called");
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional
    public UserResponse createUser(String fullName,
                                   String email,
                                   String password,
                                   String description,
                                   UserPosition position,
                                   MultipartFile cv,
                                   MultipartFile avatar) throws IOException {
        log.info("UserService.createUser called email={} position={} cvPresent={} avatarPresent={}", email, position, cv != null && !cv.isEmpty(), avatar != null && !avatar.isEmpty());
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Username is already taken");
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setDescription(description);
        user.setPosition(position);
        if (cv != null && !cv.isEmpty()) {
            String path = fileStorageService.saveCv(cv);
            user.setCvPath(path);
        }
        if (avatar != null && !avatar.isEmpty()) {
            String path = fileStorageService.saveAvatar(avatar);
            user.setAvatarPath(path);
        }
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Transactional
    public UserResponse updateUser(Long id,
                                   String fullName,
                                   String email,
                                   String password,
                                   String description,
                                   UserPosition position,
                                   MultipartFile cv,
                                   MultipartFile avatar) throws IOException {
        log.info("UserService.updateUser called id={}, description={}", id,  description);
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(fullName != null)
            user.setFullName(fullName);
        if(email != null)
            user.setEmail(email);
        if(password != null)
            user.setPassword(passwordEncoder.encode(password));
        user.setDescription(description);
        if(position != null)
            user.setPosition(position);
        if (cv != null && !cv.isEmpty()) {
            String path = fileStorageService.saveCv(cv);
            user.setCvPath(path);
        }
        if (avatar != null && !avatar.isEmpty()) {
            String path = fileStorageService.saveAvatar(avatar);
            user.setAvatarPath(path);
        }
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("UserService.deleteUser called id={}", id);
        if (!userRepository.existsById(id)) {
            throw new UsernameNotFoundException("User not found");
        }
        userRepository.deleteById(id);
        log.info("UserService.deleteUser success id={}", id);
    }

    public Set<User> getUsersByIds(Set<Long> ids) {
        log.info("UserService.getUsersByIds called count={}", ids != null ? ids.size() : 0);
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        return new HashSet<>(userRepository.findAllById(ids));
    }

    public User getUserByIdOrThrow(Long id) {
        log.info("UserService.getUserByIdOrThrow called id={}", id);
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}