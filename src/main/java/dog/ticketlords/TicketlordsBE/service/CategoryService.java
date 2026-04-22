package dog.ticketlords.TicketlordsBE.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import dog.ticketlords.TicketlordsBE.dbentity.Category;
import dog.ticketlords.TicketlordsBE.repositories.CategoryRepository;

@Service
public class CategoryService {

  CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  /**
   * Gets a category by that category's id.
   *
   * @param categoryId the id of the category.
   * @return the category whose id corresponds with the param categoryId.
   */
  public Optional<Category> getCategoryByCategoryId(long categoryId) {
    return this.categoryRepository.findById(categoryId);
  }

  /**
   * Gets a category by that category's name.
   *
   * @param categoryName the name of the category.
   * @return the category whose name corresponds with the param categoryName.
   */
  public Optional<Category> getCategoryByCategoryName(String categoryName) {
    return this.categoryRepository.findByCategoryName(categoryName);
  }

  /**
   * Gets a list of all categories present in the database.
   *
   * @return {@link List} of all categories.
   */
  public List<Category> getAllCategories() {
    return this.categoryRepository.findAll();
  }

  /**
   * Adds a category if an identical category does not already exist.
   *
   * @param category the category object to add.
   * @return true if successfully added, false otherwise.
   */
  public boolean addCategory(String categoryName) {
    if (!this.categoryRepository.existsByCategoryNameIgnoreCase(categoryName)) {
      this.categoryRepository.save(new Category(categoryName));
      return true;
    } else {
      return false;
    }
  }

  /**
   * Deletes a category from the database by that category's id.
   *
   * @param categoryId the id of the category to delete.
   * @return true if successfully deleted, false otherwise.
   */
  public boolean deleteCategoryById(long categoryId) {
    if (this.categoryRepository.existsById(categoryId)) {
      this.categoryRepository.deleteById(categoryId);
      return true;
    }
    return false;
  }

  /**
   * Finds all categories whose name contains the substringCategoryName.
   * <h1>example: search sp can find both Sports, and Spearfishing.</h1>
   *
   * @param substringCategoryName the substring to find categories based on.
   * @return {@link List} of all categories whose name contain the
   *         substringCategoryName param.
   */
  public List<Category> findAllCategoriesMatchingSubstringName(String substringCategoryName) {
    return this.categoryRepository.findByCategoryNameContainingIgnoreCase(substringCategoryName);
  }
}
