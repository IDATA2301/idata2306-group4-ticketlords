package dog.ticketlords.TicketlordsBE.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dog.ticketlords.TicketlordsBE.dbentity.Wishlist;
import dog.ticketlords.TicketlordsBE.service.WishlistService;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {

  private final WishlistService wishlistService;

  public WishlistController(WishlistService wlService) {
    this.wishlistService = wlService;
  }

  @GetMapping("/all")
  public ResponseEntity<List<Wishlist>> getAll() {
    if (wishlistService.getAllWishlists().size() >= 1) {
      return ResponseEntity.ok(wishlistService.getAllWishlists());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<List<Wishlist>> getAllUsersWishes(@PathVariable long userId) {
    List<Wishlist> wishes = this.wishlistService.getAllUsersWishes(userId);
    if (!wishes.isEmpty()) {
      return ResponseEntity.ok(wishes);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/users/{userId}/events/{eventId}")
  public ResponseEntity<Wishlist> getWish(@PathVariable int userId, @PathVariable int eventId) {
    if (wishlistService.getWish(userId, eventId).isPresent()) {
      return ResponseEntity.ok(wishlistService.getWish(userId, eventId).get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/users/{userId}/events/{eventId}")
  public ResponseEntity<Void> insertOneIntoDatabase(@PathVariable int userId, @PathVariable int eventId) {
    boolean inserted = wishlistService.insertOneIntoDatabase(userId, eventId);
    if (inserted) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }

  }

  @DeleteMapping("/users/{userId}/events/{eventId}")
  public ResponseEntity<Void> remove(@PathVariable long userId, @PathVariable long eventId) {
    boolean removed = wishlistService.removeWishlist(userId, eventId);
    if (!removed) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
