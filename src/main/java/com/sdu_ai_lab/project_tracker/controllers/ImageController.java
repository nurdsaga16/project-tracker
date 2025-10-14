package com.sdu_ai_lab.project_tracker.controllers;

import com.sdu_ai_lab.project_tracker.entities.ImageEntity;
import com.sdu_ai_lab.project_tracker.repositories.ImageRepository;
import com.sdu_ai_lab.project_tracker.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {
    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    private final FileStorageService fileStorageService;
    private final ImageRepository imageRepository;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false) String folder
    ) throws IOException {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File is required"));
        }
        String folderName = folder != null ? folder : "general";
        String relativePath = fileStorageService.saveProjectImage(folderName, file);
        ImageEntity image = ImageEntity.builder()
                .fileName(file.getOriginalFilename() != null ? file.getOriginalFilename() : "image")
                .relativePath(relativePath)
                .build();
        ImageEntity saved = imageRepository.save(image);
        log.info("Uploaded image id={}, path={}", saved.getId(), saved.getRelativePath());
        return ResponseEntity.ok(Map.of(
                "id", saved.getId(),
                "fileName", saved.getFileName(),
                "relativePath", saved.getRelativePath()
        ));
    }
}
