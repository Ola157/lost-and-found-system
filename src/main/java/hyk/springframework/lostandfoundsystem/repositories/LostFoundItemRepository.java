package hyk.springframework.lostandfoundsystem.repositories;

import hyk.springframework.lostandfoundsystem.domain.LostFoundItem;
import hyk.springframework.lostandfoundsystem.enums.Category;
import hyk.springframework.lostandfoundsystem.enums.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Htoo Yanant Khin
 */
public interface LostFoundItemRepository extends JpaRepository<LostFoundItem, UUID> {

    Optional<LostFoundItem> findById(UUID itemId);

    Long countLostFoundItemByType(Type type);

    List<LostFoundItem> findAllByUserId(Integer userId);
    
    // Filter methods
    List<LostFoundItem> findByCategory(Category category);
    
    List<LostFoundItem> findByType(Type type);
    
    List<LostFoundItem> findByCategoryAndType(Category category, Type type);
    
    List<LostFoundItem> findByUserIdAndCategory(Integer userId, Category category);
    
    List<LostFoundItem> findByUserIdAndType(Integer userId, Type type);
    
    List<LostFoundItem> findByUserIdAndCategoryAndType(Integer userId, Category category, Type type);
}
