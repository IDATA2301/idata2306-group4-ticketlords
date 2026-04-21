package dog.ticketlords.TicketlordsBE.controller;

import dog.ticketlords.TicketlordsBE.dbentity.Category;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dog.ticketlords.TicketlordsBE.service.CategoryService;

import java.net.URI;
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
  public ResponseEntity<List<Category>> getAllCategories() {
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
  @GetMapping("/{categoryId}")
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

  @GetMapping("/search")
  public ResponseEntity<List<Category>> search(@RequestParam String name) {
      List<Category> matches = categoryService.findAllCategoriesMatchingSubstringName(name);
      return matches.isEmpty()
              ? ResponseEntity.notFound().build()
              : ResponseEntity.ok(matches);
  }


  /**
   * Adds a category if an identical category does not already exist.
   *
   * @param category the category object to add.
   * @return ResponseEntity with created status and location URI, or bad request if insertion fails
   */
  @PostMapping("/category")
  public ResponseEntity<Void> addCategory(@Valid @RequestBody Category category) {
      if (this.categoryService.addCategory(category)) {
        return ResponseEntity.created(URI.create("/categories/category" + category.getCategoryId())).build();
      } else {
        return ResponseEntity.badRequest().build();
      }
  }



  @DeleteMapping("/category/{categoryId}")
  public ResponseEntity<Void> deleteCategoryById(@PathVariable int categoryId) {
    boolean removed = this.categoryService.deleteCategoryById(categoryId);
    if (!removed) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }


  }

