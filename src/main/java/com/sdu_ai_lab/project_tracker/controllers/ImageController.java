package com.sdu_ai_lab.project_tracker.controllers;

import com.sdu_ai_lab.project_tracker.entities.Image;
import com.sdu_ai_lab.project_tracker.repositories.ImageRepository;
import com.sdu_ai_lab.project_tracker.repositories.ProjectRepository;
import com.sdu_ai_lab.project_tracker.services.FileStorageService;
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
@RequestMapping("/api/v1/images")
public class ImageController {
    private final FileStorageService fileStorageService;
    private final ImageRepository imageRepository;
    private final ProjectRepository projectRepository;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("projectId") Long projectId,
            @RequestParam(value = "folder", required = false) String folder
    ) throws IOException {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File is required"));
        }
        String folderName = folder != null ? folder : "general";
        String relativePath = fileStorageService.saveProjectImage(folderName, file);
        var project = projectRepository.findById(projectId).orElseThrow();
        Image image = Image.builder()
                .relativePath(relativePath)
                .project(project)
                .build();
        Image saved = imageRepository.save(image);
        log.info("Uploaded image id={}, path={}, projectId={}", saved.getId(), saved.getRelativePath(), projectId);
        return ResponseEntity.ok(Map.of(
                "id", saved.getId(),
                "relativePath", saved.getRelativePath()
        ));
    }
}
