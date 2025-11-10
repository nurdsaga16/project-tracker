package com.sdu_ai_lab.project_tracker.entities;

import com.sdu_ai_lab.project_tracker.enums.UserPosition;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "avatar_path", nullable = true)
    private String avatarPath;

    @Column(name = "cv_path",  nullable = true)
    private String cvPath;

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false)
    private UserPosition position;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private Set<Project> authoredProjects = new HashSet<>();

    @ManyToMany(mappedBy = "teamMembers", fetch = FetchType.LAZY)
    private Set<Project> projects = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
