package com.sdu_ai_lab.project_tracker.services;

import com.sdu_ai_lab.project_tracker.dto.responses.TagResponse;
import com.sdu_ai_lab.project_tracker.entities.Tag;
import com.sdu_ai_lab.project_tracker.mappers.TagMapper;
import com.sdu_ai_lab.project_tracker.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public List<TagResponse> getAllTags() {
        log.info("TagService.getAllTags called");
        return tagRepository.findAll().stream()
                .map(tagMapper::toDto)
                .toList();
    }

    public Set<Tag> buildTags(Set<Long> tagIds, List<String> newTags) {
        log.info("TagService.buildTags called tagIdsCount={} newTagsCount={}", tagIds != null ? tagIds.size() : 0, newTags != null ? newTags.size() : 0);
        Set<Tag> tags = new HashSet<>();
        if (tagIds != null && !tagIds.isEmpty()) {
            tags.addAll(tagRepository.findAllById(tagIds));
        }
        if (newTags != null) {
            for (String name : newTags) {
                if (name == null || name.isBlank()) continue;
                Tag tag = new Tag();
                tag.setName(name);
                tags.add(tagRepository.save(tag));
            }
        }
        return tags;
    }
}
