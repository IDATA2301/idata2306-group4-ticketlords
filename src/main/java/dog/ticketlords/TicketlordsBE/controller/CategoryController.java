package dog.ticketlords.TicketlordsBE.controller;

import dog.ticketlords.TicketlordsBE.dbentity.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dog.ticketlords.TicketlordsBE.service.CategoryService;

import java.util.List;

/**
 * REST controller for category management operations.
 * <p>
 * Handles GET, POST, and DELETE requests for managing categories in the database.
 * Provides endpoints to retrieve categories...
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  /**
   * Retrieves all categories from the database.
   *
   * @return ResponseEntity containing a list of all categories, or not found if no categories exist.
   */
  @GetMapping("/")
  public ResponseEntity<List<Category>> getAllEvents() {
    if (this.categoryService.getAllCategories().size() >= 1) {
      return ResponseEntity.ok(this.categoryService.getAllCategories());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Retrieves a specific category using event ID.
   *
   * @param categoryId the ID of the event to retrieve
   * @return ResponseEntity containing the category, or not found if event does not exist
   */
  @GetMapping("/categories/{categoryId}")
  public ResponseEntity<Category> getCategory(@PathVariable long categoryId) {
    if (this.categoryService.getCategoryByCategoryId(categoryId).isPresent()) {
      return ResponseEntity.ok(categoryService.getCategoryByCategoryId(categoryId).get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Retrieves a category by its name.
   *
   * @param categoryName the category name to look up
   * @return {@link ResponseEntity} containing the {@link Category} if found; otherwise 404 \(Not Found\)
   */
    @GetMapping("/name/{categoryName}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String categoryName) {
      return categoryService.getCategoryByCategoryName(categoryName)
              .map(ResponseEntity::ok)
              .orElseGet(() -> ResponseEntity.notFound().build());
  }


  }

