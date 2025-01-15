package com.bernardo.zapzapClone.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileService {

    @Value("${application.file.uploads.media-output.path}")
    private String mediaOutputPath;

    public String saveFile(
            @NonNull MultipartFile file,
            @NonNull String senderId) {
        final String fileUploadSubPath = "users" + File.separator + senderId;
        return uploadFile(file, fileUploadSubPath);

    }

    private String uploadFile(MultipartFile file, String fileUploadSubPath) {

        final String fileUploadPath = mediaOutputPath + File.separator + fileUploadSubPath;
        File fileFolder = new File(fileUploadPath);
        if (!fileFolder.exists()) {
            boolean created = fileFolder.mkdirs();
            if (!created) {
                log.warn("Error creating file path: {}", fileUploadPath);
                return null;
            }
        }
        final String fileExtention = getFileExtension(file.getOriginalFilename());
        final String fileNameAndPath = fileUploadPath+File.separator+System.currentTimeMillis() + fileExtention;
        Path path = Paths.get(fileNameAndPath);
        try {
            Files.write(path, file.getBytes());
            log.info("File saved: {}", fileNameAndPath);
            return fileNameAndPath;
        } catch (IOException e) {
            log.error("Error saving file: {}", fileNameAndPath);
        }
        return null;
    }

    private String getFileExtension(String originalFilename) {
        if(originalFilename == null || originalFilename.isEmpty()) {
            return "";
        }
        int dotIndex = originalFilename.lastIndexOf(".");
        if(dotIndex > 0) {
            return originalFilename.substring(dotIndex).toLowerCase();
        }

        return null;

        
    }

}
