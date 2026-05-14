package dog.ticketlords.TicketlordsBE.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dog.ticketlords.TicketlordsBE.DTO.UserInterestScoreDTO;
import dog.ticketlords.TicketlordsBE.dbentity.Event;
import dog.ticketlords.TicketlordsBE.dbentity.UserInterest;
import dog.ticketlords.TicketlordsBE.service.UserInterestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controller class for handling endpoints related to {@link UserInterest}.
 */
@RestController
@RequestMapping("/api/user")
public class UserInterestController {

  private UserInterestService userInterestService;

  public UserInterestController(UserInterestService userInterestService) {
    this.userInterestService = userInterestService;
  }

  /**
   * Finds all interests for a user, based on their clicks in the interval of now
   * to this time last year.
   *
   * @param userId the id of the user to find the interests for.
   * @return the user's interest in this category by % interest, compared to the
   *         total interest of all
   *         categories.
   */
  @Operation(summary = "Get user interests by percentage sorted", description = "Finds all interests for a user, based on their clicks in the interval of now to this time last year. The interest is calculated by % interest, compared to the total interest of all categories.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved user interests."),
      @ApiResponse(responseCode = "404", description = "User interests not found.")
  })
  @GetMapping("/{userId}/interests")
  public ResponseEntity<List<UserInterestScoreDTO>> getUserInterestsByPercentageSorted(@PathVariable long userId) {
    List<UserInterestScoreDTO> interests = this.userInterestService.getAllCategoriesInterestScoreByUserSorted(userId);
    if (!interests.isEmpty()) {
      return ResponseEntity.ok(interests);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Registers a click on an event, and adds the event to the database.
   *
   * @param userId     the id of the user to register the interest for.
   * @param categoryId the id of the category to register the interest for.
   * @return A {@link UserInterest} to add to the database.
   * 
   */
  @Operation(summary = "Register user interest", description = "Registers a click on an event, which is used when calculating the user's interest in a category. The interest is added to the database.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully registered user interest."),
      @ApiResponse(responseCode = "400", description = "Failed to register user interest.")
  })
  @PostMapping("/{userId}/{categoryId}/interest/add")
  public ResponseEntity<Void> registerInterest(@PathVariable long userId, @PathVariable long categoryId) {
    if (this.userInterestService.addUserInterestEntry(userId, categoryId)) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * Gets events to recommend to a user.
   *
   * @param userId the id to find events to recommend.
   * @return the events.
   */
  @Operation(summary = "Get recommended events for a user", description = "Gets events to recommend to a user based on their interests.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved recommended events."),
      @ApiResponse(responseCode = "500", description = "Failed to retrieve recommended events.")
  })
  @GetMapping("/{userId}/recommended-events")
  public ResponseEntity<List<Event>> getUserRecommendations(@PathVariable long userId) {
    List<Event> events = this.userInterestService.getRecommendedEvents(userId);
    if (events.isEmpty()) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.ok(events);

  }

  /**
   * Finds all interests of a user since they were registered, sorted by date
   * desc.
   *
   * @param userId the id of the user to find interests for.
   * @return A sorted list of all interests for a user.
   */
  @Operation(summary = "Get all user interests sorted by date", description = "Finds all interests of a user since they were registered, sorted by date in descending order.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved user interests."),
      @ApiResponse(responseCode = "404", description = "User interests not found.")
  })
  @GetMapping("/{userId}/raw-interests")
  public ResponseEntity<List<UserInterest>> getAllInterestsRaw(@PathVariable long userId) {
    List<UserInterest> interests = this.userInterestService.getAllInterestRaw(userId);
    if (!interests.isEmpty()) {
      return ResponseEntity.ok(interests);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

}
