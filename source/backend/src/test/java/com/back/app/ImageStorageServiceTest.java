package com.back.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import com.back.app.service.ImageFolder;
import com.back.app.service.ImageStorageService;

public class ImageStorageServiceTest {
    private ImageStorageService imageStorageService;

    @BeforeEach
    void setup() {
        imageStorageService = new ImageStorageService();
    }

    // uspjesno spremanje slike
    @Test
    void testStoreImage_RegularCase() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "football.jpg",
                "image/jpeg",
                "test-image-content".getBytes()
        );

        String savedFilename = imageStorageService.storeImage(
                file,
                "football_ball",
                ImageFolder.ADVERTISEMENT
        );

        assertTrue(savedFilename.contains(".jpg"));
        Path savedFilePath = imageStorageService
                .getUploadDir(ImageFolder.ADVERTISEMENT)
                .resolve(savedFilename);

        assertTrue(Files.exists(savedFilePath));
    }

    // spremanje prazne slike - baca iznimku
    @Test
    void testStoreImage_EmptyFile_ThrowsException() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.jpg",
                "image/jpg",
                new byte[0]
        );

        Exception e = assertThrows(IOException.class, () -> {
            imageStorageService.storeImage(
                    emptyFile,
                    "empty_test",
                    ImageFolder.ACCOUNT
            );
        });

        assertEquals("Failed to store empty file.", e.getMessage());
    }

    // ucitavanje nepostojece slike
    @Test
    void testLoadImage_NonExistingFile_ThrowsException() {
        Exception e = assertThrows(IOException.class, () -> {
            imageStorageService.loadImage(
                    "does_not_exist.png",
                    ImageFolder.ADVERTISEMENT
            );
        });

        assertTrue(e.getMessage().contains("File not found"));
    }

    // folder nije validan
    @Test
    void testStoreImage_InvalidFolder_ThrowsException() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "helmet.jpg",
                "image/jpeg",
                "helmet".getBytes()
        );

        // Simuliramo nepostojeci folder - obrišemo entry iz mape
        imageStorageService.getUploadDir(ImageFolder.ACCOUNT).toFile().delete();
        //uklanjamo path iz mape:
        var uploadDirsField = ImageStorageService.class.getDeclaredField("uploadDirs");
        uploadDirsField.setAccessible(true);
        Map<ImageFolder, Path> uploadDirsMap = (Map<ImageFolder, Path>) uploadDirsField.get(imageStorageService);

        uploadDirsMap.put(ImageFolder.ACCOUNT, null); // sada folder efektivno ne postoji

        Exception e = assertThrows(IOException.class, () -> {
            imageStorageService.storeImage(
                    file,
                    "helmet",
                    ImageFolder.ACCOUNT
            );
        });

        assertTrue(e.getMessage().contains("Invalid image folder type"));
    }
}
