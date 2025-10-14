package com.sdu_ai_lab.project_tracker.repositories;

import com.sdu_ai_lab.project_tracker.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {
}
