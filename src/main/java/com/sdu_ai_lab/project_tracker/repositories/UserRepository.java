package com.sdu_ai_lab.project_tracker.repositories;

import com.sdu_ai_lab.project_tracker.dto.UserDto;
import com.sdu_ai_lab.project_tracker.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
    UserDto getUserByUsername(String username) throws UsernameNotFoundException;
}
