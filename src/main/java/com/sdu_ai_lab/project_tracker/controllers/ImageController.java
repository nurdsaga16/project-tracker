package com.sdu_ai_lab.project_tracker.controllers;

import com.sdu_ai_lab.project_tracker.services.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("${api.version}/images")
public class ImageController {
    private final ImageService imageService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("projectId") Long projectId
    ) throws IOException {
        try {
            var response = imageService.uploadImage(file, projectId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
