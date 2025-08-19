package hyk.springframework.lostandfoundsystem.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Service for handling file uploads
 * @author Htoo Yanant Khin
 */
@Service
@Slf4j
public class FileUploadService {
    
    private static final String UPLOAD_DIR = "uploads/images/";
    
    public FileUploadService() {
        // Create upload directory if it doesn't exist
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            log.debug("Upload directory path: {}", uploadPath.toAbsolutePath());
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.debug("Created upload directory: {}", uploadPath.toAbsolutePath());
            } else {
                log.debug("Upload directory already exists: {}", uploadPath.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Could not create upload directory", e);
        }
    }
    
    /**
     * Upload an image file and return the filename
     * @param file The uploaded file
     * @return The saved filename
     * @throws IOException if file cannot be saved
     */
    public String uploadImage(MultipartFile file) throws IOException {
        log.debug("Starting image upload process");
        
        if (file.isEmpty()) {
            log.debug("File is empty, returning null");
            return null;
        }
        
        log.debug("File name: {}, size: {}, content type: {}", 
                 file.getOriginalFilename(), file.getSize(), file.getContentType());
        
        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            log.error("Invalid file type: {}", contentType);
            throw new IllegalArgumentException("Only image files are allowed");
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = UUID.randomUUID().toString() + fileExtension;
        
        log.debug("Generated filename: {}", filename);
        
        // Save file
        Path uploadPath = Paths.get(UPLOAD_DIR);
        Path filePath = uploadPath.resolve(filename);
        
        log.debug("Saving file to: {}", filePath.toAbsolutePath());
        
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        log.debug("File uploaded successfully: {}", filename);
        return filename;
    }
    
    /**
     * Delete an image file
     * @param filename The filename to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteImage(String filename) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }
        
        try {
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Could not delete file: {}", filename, e);
            return false;
        }
    }
    
    /**
     * Get the upload directory path
     * @return The upload directory path
     */
    public String getUploadDir() {
        return UPLOAD_DIR;
    }
} 