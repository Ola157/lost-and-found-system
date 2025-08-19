package hyk.springframework.lostandfoundsystem.services;

import hyk.springframework.lostandfoundsystem.domain.LostFoundItem;
import hyk.springframework.lostandfoundsystem.enums.Category;
import hyk.springframework.lostandfoundsystem.enums.Type;

import java.util.List;
import java.util.UUID;

public interface LostFoundItemService {

    List<LostFoundItem> findAllItems();

    List<LostFoundItem> findAllItemsByUserId(Integer userId);

    LostFoundItem findItemById(UUID itemId);

    LostFoundItem saveItem(LostFoundItem lostFoundItem);

    void deleteItemById(UUID itemId);

    Long countItemByType(Type type);
    
    // Filter methods
    List<LostFoundItem> findItemsByCategory(Category category);
    
    List<LostFoundItem> findItemsByType(Type type);
    
    List<LostFoundItem> findItemsByCategoryAndType(Category category, Type type);
    
    List<LostFoundItem> findItemsByUserIdAndCategory(Integer userId, Category category);
    
    List<LostFoundItem> findItemsByUserIdAndType(Integer userId, Type type);
    
    List<LostFoundItem> findItemsByUserIdAndCategoryAndType(Integer userId, Category category, Type type);
}
