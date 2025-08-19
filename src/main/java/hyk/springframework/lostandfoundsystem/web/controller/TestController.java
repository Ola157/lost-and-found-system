package hyk.springframework.lostandfoundsystem.web.controller;

import hyk.springframework.lostandfoundsystem.domain.LostFoundItem;
import hyk.springframework.lostandfoundsystem.services.LostFoundItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Temporary test controller for debugging
 */
@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {
    
    private final LostFoundItemService lostFoundItemService;
    
    @GetMapping("/items")
    public String testItems(Model model) {
        log.debug("Testing items in database");
        List<LostFoundItem> items = lostFoundItemService.findAllItems();
        
        for (LostFoundItem item : items) {
            log.debug("Item ID: {}, Title: {}, ImageFileName: {}", 
                     item.getId(), item.getTitle(), item.getImageFileName());
        }
        
        model.addAttribute("items", items);
        return "test/items";
    }
    
    @GetMapping("/upload-dir")
    public String testUploadDir(Model model) {
        log.debug("Testing upload directory");
        // This will trigger the FileUploadService constructor and create the directory
        return "test/upload-dir";
    }
    
    @GetMapping("/form-test")
    public String testForm() {
        return "test/form-test";
    }
    
    @PostMapping("/form-submission")
    public String testFormSubmission(@RequestParam(value = "image", required = false) MultipartFile image,
                                   @RequestParam Map<String, String> allParams) {
        log.debug("TestController - Testing form submission");
        log.debug("Image: {}", image != null ? image.getOriginalFilename() : "null");
        log.debug("All parameters: {}", allParams);
        return "redirect:/test/items";
    }
} 