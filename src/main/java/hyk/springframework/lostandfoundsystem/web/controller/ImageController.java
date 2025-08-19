package hyk.springframework.lostandfoundsystem.web.controller;

import hyk.springframework.lostandfoundsystem.services.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Controller for serving uploaded images
 * @author Htoo Yanant Khin
 */
@Controller
@RequestMapping("/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    
    private final FileUploadService fileUploadService;
    
    /**
     * Serve uploaded images
     * @param filename The image filename
     * @return The image resource
     */
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        log.debug("Serving image: {}", filename);
        try {
            Path filePath = Paths.get(fileUploadService.getUploadDir()).resolve(filename).normalize();
            log.debug("Looking for image at: {}", filePath.toAbsolutePath());
            
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                log.debug("Image found and readable: {}", filename);
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // You might want to detect this dynamically
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                log.warn("Image not found or not readable: {}", filename);
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            log.error("Error serving image: {}", filename, e);
            return ResponseEntity.notFound().build();
        }
    }
} 