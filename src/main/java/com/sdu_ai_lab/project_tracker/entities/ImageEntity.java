package com.sdu_ai_lab.project_tracker.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Original file name (optional)
    @Column(name = "file_name", nullable = false)
    private String fileName;

    // Relative path like images/{project-title}/xyz.jpg
    @Column(name = "relative_path", nullable = false)
    private String relativePath;

    // Many-to-Many with projects
    @ManyToMany(mappedBy = "images")
    private Set<ProjectEntity> projects = new HashSet<>();
}
