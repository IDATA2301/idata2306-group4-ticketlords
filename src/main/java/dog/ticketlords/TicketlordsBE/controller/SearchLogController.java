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

@RestController
@RequestMapping("/search-logs")
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
