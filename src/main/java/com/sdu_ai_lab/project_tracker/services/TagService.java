package com.sdu_ai_lab.project_tracker.services;

import com.sdu_ai_lab.project_tracker.dto.TagDto;
import com.sdu_ai_lab.project_tracker.mappers.TagMapper;
import com.sdu_ai_lab.project_tracker.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private static final Logger log = LoggerFactory.getLogger(TagService.class);

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public List<TagDto> getAllTags() {
        log.info("Getting all tags...");
        return tagRepository.findAll().stream()
                .map(tagMapper::toDto)
                .toList();
    }
}
