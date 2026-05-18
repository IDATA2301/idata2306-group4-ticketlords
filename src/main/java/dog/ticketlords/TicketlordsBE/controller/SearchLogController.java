package dog.ticketlords.TicketlordsBE.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dog.ticketlords.TicketlordsBE.dbentity.SearchLog;
import dog.ticketlords.TicketlordsBE.service.SearchLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/search-logs")
@Tag(name = "Search Log Controller", description = "APIs for logging and retrieving user search queries. Unused functionality, and is therefore deprecated as per this date.")
public class SearchLogController {

  private SearchLogService searchLogService;

  public SearchLogController(SearchLogService searchLogService) {
    this.searchLogService = searchLogService;
  }

  /**
   * Saves a search entry for a user.
   *
   * @param userId the user performing the search (registered or unregistered)
   * @param query  the search query entered by the user
   * @return ResponseEntity with status 201 if saved successfully
   */
  @Operation(summary = "Save a search log entry", description = "Logs a search query for a user. The user can be either registered or unregistered. Returns 201 if the search log is saved successfully, or 404 if the user is not found.", deprecated = true)
  @PostMapping
  public ResponseEntity<String> saveSearch(@PathVariable long userId, @RequestParam String query) {
    boolean saved = this.searchLogService.saveSearchLog(userId, query);
    if (saved) {
      return ResponseEntity.status(HttpStatus.CREATED).body("Search logged successfully");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
  }

  /**
   * Retrieves all searches performed by a specific user.
   *
   * @param userId the id of the user to retrieve searches for.
   * @return {@link ResponseEntity} containing a list of all SearchLog entries for
   *         the user.
   */
  @Operation(summary = "Get all searches for a user", description = "Retrieves a list of all search logs for the specified user ID.", deprecated = true)
  @GetMapping("/user")
  public ResponseEntity<List<SearchLog>> getAllUserSearches(@RequestParam long userId) {
    List<SearchLog> searches = this.searchLogService.getAllUsersSearches(userId);
    return ResponseEntity.ok(searches);
  }

  /**
   * Retrieves the most recent searches performed by a user.
   *
   * @param userId the ID of the user to retrieve searches for
   * @param limit  the maximum number of recent searches to retrieve (default: 10)
   * @return ResponseEntity containing a list of recent SearchLog entries
   */
  @Operation(summary = "Get recent searches for a user", description = "Retrieves a list of the most recent search logs for the specified user ID. The number of recent searches returned can be limited using the 'limit' query parameter (default is 10).", deprecated = true)
  @GetMapping("/recent")
  public ResponseEntity<List<SearchLog>> getRecentSearches(@RequestParam long userId,
      @RequestParam(defaultValue = "10") int limit) {
    if (limit <= 0) {
      return ResponseEntity.badRequest().build();
    }
    List<SearchLog> recentSearches = this.searchLogService.getRecentSearchLogsByUserId(userId, limit);
    return ResponseEntity.ok(recentSearches);
  }

}
