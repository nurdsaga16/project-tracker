package com.sdu_ai_lab.project_tracker.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;

@Service
public class FileStorageService {

    @Value("${app.images.base-dir:images}")
    private String baseDir;

    public String saveProjectImage(String projectFolderName, MultipartFile file) throws IOException {
        String safeProject = toSafeName(projectFolderName);
        String safeFile = toSafeName(file.getOriginalFilename() != null ? file.getOriginalFilename() : "image");
        Path dir = Paths.get(baseDir, safeProject);
        Files.createDirectories(dir);
        Path target = dir.resolve(safeFile);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        // return relative path (for serving via resource handler): images/{project}/{file}
        return Paths.get(baseDir, safeProject, safeFile).toString().replace('\\','/');
    }

    private String toSafeName(String input) {
        if (input == null) return "file";
        String normalized = input;
        return normalized.replaceAll("[^a-zA-Z0-9._-]+", "-").replaceAll("-+", "-").replaceAll("^-|-$", "");
    }
}
