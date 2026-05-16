package dog.ticketlords.TicketlordsBE.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dog.ticketlords.TicketlordsBE.dbentity.Wishlist;
import dog.ticketlords.TicketlordsBE.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for wishlist management operations.
 * 
 * Handles GET, POST, and DELETE requests for managing wishlists in the
 * database.
 * Provides endpoints to retrieve wishlists by user and event, add or remove
 * items from wishlists,
 * and retrieve wishlist statistics.
 */
@RestController
@RequestMapping("/wishlists")
@Tag(name = "Wishlist Controller", description = "APIs for managing wishlists, including retrieval, addition, and removal of wishlist items, as well as checks related to wishlists.")
public class WishlistController {

  @Value("${wishlist.page-size:8}")
  private int pageSize;
  private final WishlistService wishlistService;

  /**
   * Constructs a WishlistController with the provided WishlistService.
   * 
   * @param wlService the wishlist service to be used
   */
  public WishlistController(WishlistService wlService) {
    this.wishlistService = wlService;
  }

  /**
   * Retrieves all wishlists from the database.
   * 
   * @return ResponseEntity containing a list of all wishlists, or not found if no
   *         wishlists exist
   */
  @Operation(summary = "Get all wishlists", description = "Retrieves a list of all wishlists in the database.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved all wishlists."),
      @ApiResponse(responseCode = "404", description = "No wishlists found in the database.")
  })
  @GetMapping("/")
  public ResponseEntity<List<Wishlist>> getAll() {
    if (wishlistService.getAllWishlists().size() >= 1) {
      return ResponseEntity.ok(wishlistService.getAllWishlists());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Checks if the event with eventId in the param is wishlisted of the user with
   * param userId.
   *
   * @param userId  the user we're checking wishlisted event for.
   * @param eventId the event we're checking if is wishlisted by the user.
   *
   * @return true if wishlisted, false if now.
   */
  @Operation(summary = "Check if event is wishlisted by user", description = "Checks if the event with eventId in the param is wishlisted of the user with param userId.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully checked if event is wishlisted by user."),
      @ApiResponse(responseCode = "404", description = "User or event not found.")
  })
  @GetMapping("is-wishlisted/{userId}/{eventId}")
  public ResponseEntity<Boolean> isEventWishlisted(@PathVariable long userId, @PathVariable long eventId) {
    boolean isWishlisted = this.wishlistService.isEventInWishlist(userId, eventId);
    return ResponseEntity.ok(isWishlisted);
  }

  /**
   * Retrieves all wishlists for a specific user.
   * 
   * @param userId the ID of the user
   * @return ResponseEntity containing a list of all wishes for the user, or not
   *         found if no wishes exist
   */
  @Operation(summary = "Get all wishlists for a user", description = "Retrieves a list of all wishlists for a specific user based on the provided user ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved all wishlists for the user."),
      @ApiResponse(responseCode = "404", description = "No wishlists found for the user with the specified ID.")
  })
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Wishlist>> getAllUsersWishes(@PathVariable long userId) {
    List<Wishlist> wishes = this.wishlistService.getAllUsersWishes(userId);
    if (!wishes.isEmpty()) {
      return ResponseEntity.ok(wishes);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Retrieves paginated wishlists for a specific user.
   * 
   * @param userId the ID of the user
   * @param page   the page number to retrieve
   * @return ResponseEntity containing a paginated list of wishes for the user
   */
  @Operation(summary = "Get paginated wishlists for a user", description = "Retrieves a paginated list of wishlists for a specific user based on the provided user ID and page number.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated wishlists for the user."),
      @ApiResponse(responseCode = "404", description = "No wishlists found for the user with the specified ID and page number.")
  })
  @GetMapping("/user/{userId}/{page}")
  public ResponseEntity<List<Wishlist>> getUserWishlistingsPaged(@PathVariable long userId, @PathVariable int page) {
    Page<Wishlist> wishes = this.wishlistService.getUsersWishlistingsPaged(userId, page, this.pageSize);
    return ResponseEntity.ok(wishes.toList());
  }

  /**
   * Retrieves a specific wish for a user by user ID and event ID.
   * 
   * @param userId  the ID of the user
   * @param eventId the ID of the event
   * @return ResponseEntity containing the wish, or not found if wish does not
   *         exist
   */
  @Operation(summary = "Get a specific wish for a user", description = "Retrieves a specific wish for a user based on the provided user ID and event ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the wish for the user."),
      @ApiResponse(responseCode = "404", description = "Wish not found for the user with the specified user ID and event ID.")
  })
  @GetMapping("/user/{userId}/event/{eventId}")
  public ResponseEntity<Wishlist> getWish(@PathVariable int userId, @PathVariable int eventId) {
    Optional<Wishlist> wish = this.wishlistService.getWish(userId, eventId);
    if (wish.isPresent()) {
      return ResponseEntity.ok(wish.get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Retrieves the number of wishlists for a specific event.
   * 
   * @param eventId the ID of the event
   * @return ResponseEntity containing the count of wishlists for the event
   */
  @Operation(summary = "Get wishlist count for an event", description = "Retrieves the number of wishlists for a specific event based on the provided event ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the wishlist count for the event."),
      @ApiResponse(responseCode = "404", description = "Event not found.")
  })
  @GetMapping("/event/{eventId}")
  public ResponseEntity<Long> getWishListingAmountForEventById(@PathVariable long eventId) {
    return ResponseEntity.ok(this.wishlistService.getWishlistingAmountForEventById(eventId));
  }

  /**
   * Retrieves the total number of wishes for a specific user.
   * 
   * @param userId the ID of the user
   * @return ResponseEntity containing the count of wishes for the user
   */
  @Operation(summary = "Get total wish count for a user", description = "Retrieves the total number of wishes for a specific user based on the provided user ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the total wish count for the user."),
      @ApiResponse(responseCode = "404", description = "User not found.")
  })
  @GetMapping("/user/{userId}/amount")
  public ResponseEntity<Long> getUserAmountOfWishes(@PathVariable long userId) {
    return ResponseEntity.ok(this.wishlistService.countWishesByUser(userId));
  }

  /**
   * Adds an event to a user's wishlist.
   * 
   * @param userId  the ID of the user
   * @param eventId the ID of the event to add
   * @return ResponseEntity with ok status if successful, or not found if user or
   *         event does not exist
   */
  @Operation(summary = "Add event to user's wishlist", description = "Adds an event to a user's wishlist based on the provided user ID and event ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully added the event to the user's wishlist."),
      @ApiResponse(responseCode = "404", description = "User or event not found.")
  })
  @PostMapping("/user/{userId}/event/{eventId}")
  public ResponseEntity<Void> insertOneIntoDatabase(@PathVariable int userId, @PathVariable int eventId) {
    boolean inserted = wishlistService.insertOneIntoDatabase(userId, eventId);
    if (inserted) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Removes an event from a user's wishlist.
   * 
   * @param userId  the ID of the user
   * @param eventId the ID of the event to remove
   * @return ResponseEntity with no content status if successful, or not found if
   *         wish does not exist
   */
  @Operation(summary = "Remove event from user's wishlist", description = "Removes an event from a user's wishlist based on the provided user ID and event ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successfully removed the event from the user's wishlist."),
      @ApiResponse(responseCode = "404", description = "Wish not found for the user with the specified user ID and event ID.")
  })
  @DeleteMapping("/user/{userId}/event/{eventId}")
  public ResponseEntity<Void> remove(@PathVariable long userId, @PathVariable long eventId) {
    boolean removed = wishlistService.removeWishlist(userId, eventId);
    if (!removed) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
