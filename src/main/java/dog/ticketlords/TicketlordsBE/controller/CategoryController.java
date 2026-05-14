package dog.ticketlords.TicketlordsBE.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dog.ticketlords.TicketlordsBE.dbentity.Category;
import dog.ticketlords.TicketlordsBE.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for category management operations.
 * <p>
 * Handles GET, POST, and DELETE requests for managing categories in the
 * database.
 * Provides endpoints to retrieve categories...
 */

@Tag(name = "Categories", description = "Category management APIs")
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
   * @return ResponseEntity containing a list of all categories, or not found if
   *         no categories exist.
   */
  @Operation(summary = "Get all categories", description = "Returns all categories. If none exists, returns 404.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categories returned", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Category.class)))),
      @ApiResponse(responseCode = "404", description = "No categories found")
  })
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
   * @return ResponseEntity containing the category, or not found if event does
   *         not exist
   */
  @Operation(summary = "Get category by ID", description = "Returns the category with the given ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Category found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
      @ApiResponse(responseCode = "404", description = "Category not found")
  })

  @GetMapping("/{categoryId}")
  public ResponseEntity<Category> getCategory(
      @Parameter(description = "Category Id", required = true, example = "1") @PathVariable long categoryId) {
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
   * @return {@link ResponseEntity} containing the {@link Category} if found;
   *         otherwise 404 \(Not Found\)
   */
  @Operation(summary = "Get category by name", description = "Returns the category with the given name.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Category found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
      @ApiResponse(responseCode = "404", description = "Category not found")
  })
  @GetMapping("/name/{categoryName}")
  public ResponseEntity<Category> getCategoryByName(@PathVariable String categoryName) {
    return categoryService.getCategoryByCategoryName(categoryName)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Search categories by name substring", description = "Returns categories whose name contains the provided substring. The substring is not case sensitive. Returns 404 if no matches")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categories found", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Category.class)))),
      @ApiResponse(responseCode = "404", description = "No matching categories found")
  })
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
   * @return ResponseEntity with created status and location URI, or bad request
   *         if insertion fails
   */
  @Operation(summary = "Create a new category", description = "Adds a new category if it one with the same name doesnt already exist does not already exist. The check is done based on the category's name. Returns 201 if created, or 400 if a category with the same name already exists.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Category created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class, example = "{\"categoryId\": 1, \"categoryName\": \"new category\"}"))),
      @ApiResponse(responseCode = "400", description = "Category with the same name already exists")
  })
  @PostMapping("/category")
  public ResponseEntity<Void> addCategory(
      @Parameter(description = "Name of the category", required = true, example = "https://ticketlords-backend-app-ripdj.ondigitalocean.app/category?name= ->'new category's name' <-") @RequestParam String categoryName) {
    if (this.categoryService.addCategory(categoryName)) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  @Operation(summary = "Delete category by ID", description = "Deletes the category with the given ID. Returns 204 if deleted, or 404 if no category with the given ID exists.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Category deleted"),
      @ApiResponse(responseCode = "404", description = "Category not found")
  })
  @DeleteMapping("/category/{categoryId}")
  public ResponseEntity<Void> deleteCategoryById(@PathVariable int categoryId) {
    boolean removed = this.categoryService.deleteCategoryById(categoryId);
    if (!removed) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }

}
