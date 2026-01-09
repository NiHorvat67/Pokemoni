package com.back.app.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {
    private final Path uploadDir;
    
    public ImageStorageService() {
        this.uploadDir = Paths.get("").toAbsolutePath().resolve("images");
        
        try {
            Files.createDirectories(this.uploadDir);
            System.out.println("Upload directory created at: " + this.uploadDir.toString());
        } catch (IOException ex) {
            throw new RuntimeException("Could not create upload directory", ex);
        }
    }
    
public String storeImage(MultipartFile file, String customName) throws IOException {
    if (file.isEmpty()) {
        throw new IOException("Failed to store empty file.");
    }
    
    String originalFilename = file.getOriginalFilename();
    String extension = "";
    if (originalFilename != null && originalFilename.contains(".")) {
        extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
    }

    String filename = customName + extension;
    
    Path targetLocation = this.uploadDir.resolve(filename);
    
    try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
    }
    
    System.out.println("File saved as: " + filename);
    return filename;
}
    
    public Resource loadImage(String filename) throws IOException {
        Path filePath = this.uploadDir.resolve(filename).normalize();
        Resource resource = new FileSystemResource(filePath);
        
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("File not found: " + filename);
        }
    }
    
    public Path getUploadDir() {
        return uploadDir;
    }
    
    public String getUploadDirAsString() {
        return uploadDir.toString();
    }
    
    public boolean deleteImage(String filename) throws IOException {
        Path filePath = this.uploadDir.resolve(filename).normalize();
        return Files.deleteIfExists(filePath);
    }
}