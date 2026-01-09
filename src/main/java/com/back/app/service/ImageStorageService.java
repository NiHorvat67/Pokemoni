package com.back.app.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {

    private final Map<ImageFolder, Path> uploadDirs = new HashMap<>();

    public ImageStorageService() {
        for (ImageFolder folder : ImageFolder.values()) {
            Path dir = Paths.get("").toAbsolutePath().resolve(folder.getFolderName());
            try {
                Files.createDirectories(dir);
                uploadDirs.put(folder, dir);
                System.out.println("Created folder: " + dir.toString());
            } catch (IOException e) {
                System.err.println("Failed to create folder: " + folder.getFolderName());
            }
        }
    }

    public String storeImage(MultipartFile file, String customName, ImageFolder folder) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }

        Path uploadDir = uploadDirs.get(folder);
        if (uploadDir == null) {
            throw new IOException("Invalid image folder type: " + folder);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }

        String filename = customName + extension;
        Path targetLocation = uploadDir.resolve(filename);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        }

        System.out.println("File saved as: " + filename + " in folder: " + folder.getFolderName());
        return filename;
    }

    public Resource loadImage(String filename, ImageFolder folder) throws IOException {
        Path uploadDir = uploadDirs.get(folder);
        if (uploadDir == null) {
            throw new IOException("Invalid image folder type: " + folder);
        }

        Path filePath = uploadDir.resolve(filename).normalize();
        Resource resource = new FileSystemResource(filePath);

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("File not found: " + filename + " in folder: " + folder.getFolderName());
        }
    }

    public Path getUploadDir(ImageFolder folder) {
        return uploadDirs.get(folder);
    }

    public String getUploadDirAsString(ImageFolder folder) {
        Path dir = uploadDirs.get(folder);
        return dir != null ? dir.toString() : "";
    }

    public boolean deleteImage(String filename, ImageFolder folder) throws IOException {
        Path uploadDir = uploadDirs.get(folder);
        if (uploadDir == null) {
            throw new IOException("Invalid image folder type: " + folder);
        }

        Path filePath = uploadDir.resolve(filename).normalize();
        return Files.deleteIfExists(filePath);
    }

  
}