package dog.ticketlords.TicketlordsBE.controller;

import java.util.List;

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

/**
 * REST controller for wishlist management operations.
 * 
 * Handles GET, POST, and DELETE requests for managing wishlists in the database.
 * Provides endpoints to retrieve wishlists by user and event, add or remove items from wishlists,
 * and retrieve wishlist statistics.
 */
@RestController
@RequestMapping("/wishlists")
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
   * @return ResponseEntity containing a list of all wishlists, or not found if no wishlists exist
   */
  @GetMapping("/")
  public ResponseEntity<List<Wishlist>> getAll() {
    if (wishlistService.getAllWishlists().size() >= 1) {
      return ResponseEntity.ok(wishlistService.getAllWishlists());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Retrieves all wishlists for a specific user.
   * 
   * @param userId the ID of the user
   * @return ResponseEntity containing a list of all wishes for the user, or not found if no wishes exist
   */
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
   * @param page the page number to retrieve
   * @return ResponseEntity containing a paginated list of wishes for the user
   */
  @GetMapping("/users/{userId}/{page}")
  public ResponseEntity<List<Wishlist>> getUserWishlistingsPaged(@PathVariable long userId, @PathVariable int page) {
    Page<Wishlist> wishes = this.wishlistService.getUsersWishlistingsPaged(userId, page, this.pageSize);
    return ResponseEntity.ok(wishes.toList());
  }

  /**
   * Retrieves a specific wish for a user by user ID and event ID.
   * 
   * @param userId the ID of the user
   * @param eventId the ID of the event
   * @return ResponseEntity containing the wish, or not found if wish does not exist
   */
  @GetMapping("/users/{userId}/events/{eventId}")
  public ResponseEntity<Wishlist> getWish(@PathVariable int userId, @PathVariable int eventId) {
    if (wishlistService.getWish(userId, eventId).isPresent()) {
      return ResponseEntity.ok(wishlistService.getWish(userId, eventId).get());
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
  @GetMapping("/user/{userId}/amount")
  public ResponseEntity<Long> getUserAmountOfWishes(@PathVariable long userId) {
    return ResponseEntity.ok(this.wishlistService.countWishesByUser(userId));
  }

  /**
   * Adds an event to a user's wishlist.
   * 
   * @param userId the ID of the user
   * @param eventId the ID of the event to add
   * @return ResponseEntity with ok status if successful, or not found if user or event does not exist
   */
  @PostMapping("/user/{userId}/events/{eventId}")
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
   * @param userId the ID of the user
   * @param eventId the ID of the event to remove
   * @return ResponseEntity with no content status if successful, or not found if wish does not exist
   */
  @DeleteMapping("/user/{userId}/events/{eventId}")
  public ResponseEntity<Void> remove(@PathVariable long userId, @PathVariable long eventId) {
    boolean removed = wishlistService.removeWishlist(userId, eventId);
    if (!removed) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
