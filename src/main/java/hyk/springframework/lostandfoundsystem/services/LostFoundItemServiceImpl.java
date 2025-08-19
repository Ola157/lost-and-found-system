package hyk.springframework.lostandfoundsystem.services;

import hyk.springframework.lostandfoundsystem.domain.LostFoundItem;
import hyk.springframework.lostandfoundsystem.enums.Category;
import hyk.springframework.lostandfoundsystem.enums.Type;
import hyk.springframework.lostandfoundsystem.exceptions.ResourceNotFoundException;
import hyk.springframework.lostandfoundsystem.repositories.LostFoundItemRepository;
import hyk.springframework.lostandfoundsystem.util.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author Htoo Yanant Khin
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class LostFoundItemServiceImpl implements LostFoundItemService {

    private final LostFoundItemRepository lostFoundItemRepository;

    @Override
    public List<LostFoundItem> findAllItems() {
        log.debug("Service Layer - Find all lost/found items");
        return lostFoundItemRepository.findAll();
    }

    @Override
    public List<LostFoundItem> findAllItemsByUserId(Integer userId) {
        log.debug("Service Layer - Find lost/found items by user ID: " + userId);
        return lostFoundItemRepository.findAllByUserId(userId);
    }

    @Override
    public LostFoundItem findItemById(UUID itemId) {
        log.debug("Service Layer - Find lost/found items by item ID: " + itemId);
        return lostFoundItemRepository.findById(itemId).orElseThrow(
                () -> new ResourceNotFoundException("No Lost/Found Item for Requested ID !"));
    }

    @Override
    public LostFoundItem saveItem(LostFoundItem lostFoundItem) {
        log.debug("Service Layer - Save lost/found item with ID: " + lostFoundItem.getId());
        log.debug("Service Layer - ImageFileName before save: " + lostFoundItem.getImageFileName());
        lostFoundItem.setModifiedBy(LoginUserUtil.getLoginUser().getUsername());
        LostFoundItem savedItem = lostFoundItemRepository.save(lostFoundItem);
        log.debug("Service Layer - ImageFileName after save: " + savedItem.getImageFileName());
        return savedItem;
    }

    @Override
    public void deleteItemById(UUID itemId) {
        if (findItemById(itemId) != null) {
            log.debug("Service Layer - Delete lost/found items by item ID: " + itemId);
            lostFoundItemRepository.deleteById(itemId);
        }
    }

    @Override
    public Long countItemByType(Type type) {
        log.debug("Count lost/found items");
        return lostFoundItemRepository.countLostFoundItemByType(type);
    }
    
    // Filter method implementations
    @Override
    public List<LostFoundItem> findItemsByCategory(Category category) {
        log.debug("Service Layer - Find items by category: " + category);
        return lostFoundItemRepository.findByCategory(category);
    }
    
    @Override
    public List<LostFoundItem> findItemsByType(Type type) {
        log.debug("Service Layer - Find items by type: " + type);
        return lostFoundItemRepository.findByType(type);
    }
    
    @Override
    public List<LostFoundItem> findItemsByCategoryAndType(Category category, Type type) {
        log.debug("Service Layer - Find items by category: " + category + " and type: " + type);
        return lostFoundItemRepository.findByCategoryAndType(category, type);
    }
    
    @Override
    public List<LostFoundItem> findItemsByUserIdAndCategory(Integer userId, Category category) {
        log.debug("Service Layer - Find items by user ID: " + userId + " and category: " + category);
        return lostFoundItemRepository.findByUserIdAndCategory(userId, category);
    }
    
    @Override
    public List<LostFoundItem> findItemsByUserIdAndType(Integer userId, Type type) {
        log.debug("Service Layer - Find items by user ID: " + userId + " and type: " + type);
        return lostFoundItemRepository.findByUserIdAndType(userId, type);
    }
    
    @Override
    public List<LostFoundItem> findItemsByUserIdAndCategoryAndType(Integer userId, Category category, Type type) {
        log.debug("Service Layer - Find items by user ID: " + userId + ", category: " + category + " and type: " + type);
        return lostFoundItemRepository.findByUserIdAndCategoryAndType(userId, category, type);
    }
}
