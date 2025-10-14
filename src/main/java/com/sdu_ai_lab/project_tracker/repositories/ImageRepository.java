package com.sdu_ai_lab.project_tracker.repositories;

import com.sdu_ai_lab.project_tracker.entities.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
}
