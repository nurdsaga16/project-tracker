package com.sdu_ai_lab.project_tracker.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    @Value("${app.images.base-dir:images}")
    private String baseDir;

    @Value("${app.cv.base-dir:cv}")
    private String cvBaseDir;

    @Value("${app.avatars.base-dir:avatars}")
    private String avatarsBaseDir;

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

    public String saveCv(MultipartFile file, String preferredName) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("CV file is required");
        }
        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "cv.pdf";
        String lower = original.toLowerCase();
        String contentType = file.getContentType();
        if (!(lower.endsWith(".pdf") || (contentType != null && contentType.equalsIgnoreCase("application/pdf")))) {
            throw new IllegalArgumentException("Only PDF files are allowed for CV");
        }
        String safeFileName = toSafeName(preferredName != null ? preferredName : original);
        if (!safeFileName.toLowerCase().endsWith(".pdf")) {
            safeFileName = safeFileName + ".pdf";
        }
        Path dir = Paths.get(cvBaseDir);
        Files.createDirectories(dir);
        Path target = dir.resolve(safeFileName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return Paths.get(cvBaseDir, safeFileName).toString().replace('\\','/');
    }

    public String saveAvatar(MultipartFile file, String preferredName) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Avatar file is required");
        }
        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "avatar.png";
        String safeFileName = toSafeName(preferredName != null ? preferredName : original);
        // ensure has extension, default to .png
        if (!safeFileName.contains(".")) {
            safeFileName = safeFileName + ".png";
        }
        Path dir = Paths.get(avatarsBaseDir);
        Files.createDirectories(dir);
        Path target = dir.resolve(safeFileName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return Paths.get(avatarsBaseDir, safeFileName).toString().replace('\\','/');
    }

    private String toSafeName(String input) {
        if (input == null) return "file";
        String normalized = input;
        return normalized.replaceAll("[^a-zA-Z0-9._-]+", "-").replaceAll("-+", "-").replaceAll("^-|-$", "");
    }
}
