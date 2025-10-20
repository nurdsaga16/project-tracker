package com.sdu_ai_lab.project_tracker.repositories;

import com.sdu_ai_lab.project_tracker.dto.responses.UserResponse;
import com.sdu_ai_lab.project_tracker.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    UserResponse getUserByUsername(String username) throws UsernameNotFoundException;
}
