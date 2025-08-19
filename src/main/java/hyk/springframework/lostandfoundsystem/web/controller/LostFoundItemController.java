package hyk.springframework.lostandfoundsystem.web.controller;

import hyk.springframework.lostandfoundsystem.domain.LostFoundItem;
import hyk.springframework.lostandfoundsystem.domain.security.User;
import hyk.springframework.lostandfoundsystem.enums.Category;
import hyk.springframework.lostandfoundsystem.enums.Type;
import hyk.springframework.lostandfoundsystem.services.FileUploadService;
import hyk.springframework.lostandfoundsystem.services.LostFoundItemService;
import hyk.springframework.lostandfoundsystem.util.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author Htoo Yanant Khin
 **/
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/lostFound")
public class LostFoundItemController {
    private static final String ITEM_CREATE_OR_UPDATE_FORM = "lostfound/lostFoundReportForm";
    private static final String ALL_LOST_FOUND_ITEMS = "lostfound/allLostFoundItems";

    private final LostFoundItemService lostFoundItemService;
    private final FileUploadService fileUploadService;

    @GetMapping("/show")
    public String showAllLostFoundItems(
            @RequestParam(value = "category", required = false) String categoryParam,
            @RequestParam(value = "type", required = false) String typeParam,
            Model model) {
        log.debug("LostFoundItem Controller - Show all lost/found items with filters - category: {}, type: {}", categoryParam, typeParam);
        
        List<LostFoundItem> items;
        
        if (categoryParam != null && !categoryParam.isEmpty() && typeParam != null && !typeParam.isEmpty()) {
            // Both filters applied
            Category category = Category.valueOf(categoryParam.toUpperCase());
            Type type = Type.valueOf(typeParam.toUpperCase());
            items = lostFoundItemService.findItemsByCategoryAndType(category, type);
        } else if (categoryParam != null && !categoryParam.isEmpty()) {
            // Only category filter
            Category category = Category.valueOf(categoryParam.toUpperCase());
            items = lostFoundItemService.findItemsByCategory(category);
        } else if (typeParam != null && !typeParam.isEmpty()) {
            // Only type filter
            Type type = Type.valueOf(typeParam.toUpperCase());
            items = lostFoundItemService.findItemsByType(type);
        } else {
            // No filters
            items = lostFoundItemService.findAllItems();
        }
        
        model.addAttribute("lostFoundItems", items);
        model.addAttribute("categories", Category.values());
        model.addAttribute("types", Type.values());
        model.addAttribute("selectedCategory", categoryParam);
        model.addAttribute("selectedType", typeParam);
        
        return ALL_LOST_FOUND_ITEMS;
    }

    @GetMapping("show/current/{userId}")
    public String showByUserId(
            @PathVariable Integer userId,
            @RequestParam(value = "category", required = false) String categoryParam,
            @RequestParam(value = "type", required = false) String typeParam,
            Model model) {
        log.debug("LostFoundItem Controller - Show all lost/found items by user ID: " + userId + " with filters - category: {}, type: {}", categoryParam, typeParam);
        
        List<LostFoundItem> items;
        
        if (categoryParam != null && !categoryParam.isEmpty() && typeParam != null && !typeParam.isEmpty()) {
            // Both filters applied
            Category category = Category.valueOf(categoryParam.toUpperCase());
            Type type = Type.valueOf(typeParam.toUpperCase());
            items = lostFoundItemService.findItemsByUserIdAndCategoryAndType(userId, category, type);
        } else if (categoryParam != null && !categoryParam.isEmpty()) {
            // Only category filter
            Category category = Category.valueOf(categoryParam.toUpperCase());
            items = lostFoundItemService.findItemsByUserIdAndCategory(userId, category);
        } else if (typeParam != null && !typeParam.isEmpty()) {
            // Only type filter
            Type type = Type.valueOf(typeParam.toUpperCase());
            items = lostFoundItemService.findItemsByUserIdAndType(userId, type);
        } else {
            // No filters
            items = lostFoundItemService.findAllItemsByUserId(userId);
        }
        
        model.addAttribute("lostFoundItems", items);
        model.addAttribute("categories", Category.values());
        model.addAttribute("types", Type.values());
        model.addAttribute("selectedCategory", categoryParam);
        model.addAttribute("selectedType", typeParam);
        
        return ALL_LOST_FOUND_ITEMS;
    }

    @GetMapping("/show/{itemId}")
    public String showByItemId(@PathVariable("itemId") UUID itemId, Model model) {
        log.debug("LostFoundItem Controller - Show lost/found item by item ID: " + itemId);
        model.addAttribute("lostFoundItem", lostFoundItemService.findItemById(itemId));
        return "lostfound/lostFoundItemDetail";
    }

    // admin, user
    @GetMapping("/new")
    public String initCreateItemForm(Model model) {
        log.debug("LostFoundItem Controller - Show lost/found report creation form");
        LostFoundItem newItem = new LostFoundItem();
        model.addAttribute("lostFoundItem", newItem);
        return ITEM_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/new")
    // @Valid parameter must be followed by BindingResult parameter
    public String processCreateItemForm(@Valid LostFoundItem lostFoundItem, BindingResult result,
                                       @RequestParam(value = "image", required = false) MultipartFile image) {
        log.debug("LostFoundItem Controller - Process lost/found report creation - Start");
        log.debug("Received lostFoundItem - Title: {}, Type: {}, Category: {}, Phone: {}, Email: {}", 
                 lostFoundItem.getTitle(), lostFoundItem.getType(), lostFoundItem.getCategory(), 
                 lostFoundItem.getReporterPhoneNo(), lostFoundItem.getReporterEmail());

        // Get logged in user info
        User user = LoginUserUtil.getLoginUser();

        // Handle image upload
        String imageFileName = null;
        log.debug("Image upload parameter - image: {}, isEmpty: {}", image, image == null ? "null" : image.isEmpty());
        
        if (image != null && !image.isEmpty()) {
            try {
                imageFileName = fileUploadService.uploadImage(image);
                log.debug("Image uploaded successfully, filename: {}", imageFileName);
            } catch (Exception e) {
                log.error("Error uploading image", e);
                result.rejectValue("imageFileName", "error.image", "Failed to upload image: " + e.getMessage());
            }
        } else {
            log.debug("No image provided or image is empty");
        }

        LostFoundItem newItem = new LostFoundItem();
        newItem.setType(lostFoundItem.getType());
        newItem.setTitle(lostFoundItem.getTitle());
        newItem.setLostFoundDate(lostFoundItem.getLostFoundDate());
        newItem.setLostFoundLocation(lostFoundItem.getLostFoundLocation());
        newItem.setDescription(lostFoundItem.getDescription());
        newItem.setReporterName(lostFoundItem.getReporterName());
        newItem.setReporterEmail(lostFoundItem.getReporterEmail());
        newItem.setReporterPhoneNo(lostFoundItem.getReporterPhoneNo());
        newItem.setCategory(lostFoundItem.getCategory());
        newItem.setImageFileName(imageFileName);
        newItem.setCreatedBy(user.getUsername());
        newItem.setModifiedBy(user.getUsername());
        newItem.setUser(user);

        if (result.hasErrors()) {
            log.debug("Validation errors found: {}", result.getAllErrors());
            for (var error : result.getAllErrors()) {
                log.debug("Error: {} - {}", error.getObjectName(), error.getDefaultMessage());
            }
            return ITEM_CREATE_OR_UPDATE_FORM;
        } else {
            LostFoundItem savedItem = lostFoundItemService.saveItem(newItem);
            log.debug("LostFoundItem Controller - Process lost/found report creation - End");
            log.debug("Saved item - ID: {}, ImageFileName: {}", savedItem.getId(), savedItem.getImageFileName());
            return "redirect:/lostFound/show/" + savedItem.getId();
        }
    }

    @GetMapping("/edit/{itemId}")
    public String initUpdateItemForm(@PathVariable UUID itemId, Model model) {
        log.debug("LostFoundItem Controller - Show lost/found report update form - Start");
        LostFoundItem lostFoundItem = lostFoundItemService.findItemById(itemId);
        checkPermission(lostFoundItem);
        model.addAttribute("lostFoundItem", lostFoundItem);
        return ITEM_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/edit/{itemId}")
    // @Valid parameter must be followed by BindingResult parameter
    public String processUpdateItemForm(@PathVariable UUID itemId, @Valid LostFoundItem lostFoundItem, BindingResult result,
                                       @RequestParam(value = "image", required = false) MultipartFile image, Model model) {
        log.debug("LostFoundItem Controller - Process lost/found report update - Start");
        log.debug("Path variable itemId: {}", itemId);
        log.debug("Received lostFoundItem - ID: {}, Title: {}, Type: {}, Phone: {}, Email: {}, ImageFileName: {}", 
                 lostFoundItem.getId(), lostFoundItem.getTitle(), lostFoundItem.getType(), 
                 lostFoundItem.getReporterPhoneNo(), lostFoundItem.getReporterEmail(), lostFoundItem.getImageFileName());
        log.debug("Image file: {}", image != null ? (image.isEmpty() ? "empty" : image.getOriginalFilename()) : "null");
        
        // Get the existing item to update it directly
        LostFoundItem existingItem = lostFoundItemService.findItemById(itemId);
        log.debug("Existing item - ID: {}, ImageFileName: {}", existingItem.getId(), existingItem.getImageFileName());
        
        // Handle image upload
        String finalImageFileName = null;
        log.debug("Processing image upload - image: {}, isEmpty: {}", image != null ? image.getOriginalFilename() : "null", image != null ? image.isEmpty() : "N/A");
        if (image != null && !image.isEmpty()) {
            try {
                finalImageFileName = fileUploadService.uploadImage(image);
                log.debug("New image uploaded successfully, filename: {}", finalImageFileName);
            } catch (Exception e) {
                log.error("Error uploading image", e);
                result.rejectValue("imageFileName", "error.image", "Failed to upload image: " + e.getMessage());
            }
        } else {
            // Preserve the existing image filename if no new image is uploaded
            finalImageFileName = existingItem.getImageFileName();
            log.debug("No new image uploaded, preserving existing image: {}", finalImageFileName);
        }
        
        // Update the existing entity directly instead of creating a new one
        existingItem.setType(lostFoundItem.getType());
        existingItem.setTitle(lostFoundItem.getTitle());
        existingItem.setLostFoundDate(lostFoundItem.getLostFoundDate());
        existingItem.setLostFoundLocation(lostFoundItem.getLostFoundLocation());
        existingItem.setDescription(lostFoundItem.getDescription());
        existingItem.setReporterName(lostFoundItem.getReporterName());
        existingItem.setReporterEmail(lostFoundItem.getReporterEmail());
        existingItem.setReporterPhoneNo(lostFoundItem.getReporterPhoneNo());
        existingItem.setCategory(lostFoundItem.getCategory());
        existingItem.setImageFileName(finalImageFileName);
        existingItem.setModifiedBy(LoginUserUtil.getLoginUser().getUsername());
        // Note: We don't set createdBy, createdTimestamp, or user as they should remain unchanged
        // The lastModifiedTimestamp will be automatically updated by @UpdateTimestamp annotation
        
        log.debug("Updated existing item - ID: {}, ImageFileName: {}", 
                 existingItem.getId(), existingItem.getImageFileName());
        
        if (result.hasErrors()) {
            log.debug("Validation errors found: {}", result.getAllErrors());
            for (var error : result.getAllErrors()) {
                log.debug("Error: {} - {}", error.getObjectName(), error.getDefaultMessage());
            }
            // Add the lostFoundItem back to the model so the form can be re-displayed
            model.addAttribute("lostFoundItem", lostFoundItem);
            return ITEM_CREATE_OR_UPDATE_FORM;
        } else {
            LostFoundItem savedItem = lostFoundItemService.saveItem(existingItem);
            log.debug("LostFoundItem Controller - Process lost/found report update -End");
            log.debug("Updated item - ID: {}, ImageFileName: {}", savedItem.getId(), savedItem.getImageFileName());
            return "redirect:/lostFound/show/" + savedItem.getId();
        }
    }

    @GetMapping("/delete/{itemId}")
    public String deleteItem(@PathVariable UUID itemId) {
        checkPermission(lostFoundItemService.findItemById(itemId));
        lostFoundItemService.deleteItemById(itemId);
        log.debug("LostFoundItem Controller - Delete lost/found report by ID: " + itemId);
        return "redirect:/lostFound/show/";
    }

    /**
     * To ensure that "USER" role can only have access to its data, not other user's data
     * "ADMIN" role can have access to all users' data
     */
    private void checkPermission(LostFoundItem lostFoundItem) {
        log.debug("LostFoundItem Controller - Check permission");
        if (!LoginUserUtil.isAdmin() &&
                !lostFoundItem.getUser().getId().equals(LoginUserUtil.getLoginUser().getId())) {
            throw new AccessDeniedException("You don't have the permission to perform " +
                    "this operation on other user's data");
        }
    }
}
