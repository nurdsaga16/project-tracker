package com.sdu_ai_lab.project_tracker.controllers;

import com.sdu_ai_lab.project_tracker.dto.TagDto;
import com.sdu_ai_lab.project_tracker.services.TagService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class TagController {
    private static final Logger logger = LoggerFactory.getLogger(TagController.class);
    private final TagService tagService;

    @GetMapping()
    public ResponseEntity<List<TagDto>> getAllTags() {
        logger.info("Getting all tags...");
        List<TagDto> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }
}
