package dog.ticketlords.TicketlordsBE.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import dog.ticketlords.TicketlordsBE.entity.Wishlist;
import dog.ticketlords.TicketlordsBE.repositories.WishlistRepository;
import dog.ticketlords.TicketlordsBE.service.WishlistService;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {

  private final WishlistService wishlistService;

  public WishlistController(WishlistService wlService) {
    this.wishlistService = wlService;
  }
  
  @GetMapping
  public List<Wishlist> getAll() {
    return wishlistService.getAll();
  }

  @GetMapping("/users/{userId}")
  public List<Wishlist> getAllForUser(@PathVariable int userId) {
    return wishlistService.getAllForUser(userId);
  }

  @GetMapping("/users/{userId}/events/{eventId}")
  public Optional<Wishlist> getWish(@PathVariable int userId, @PathVariable int eventId) {
    return wishlistService.getWish(userId, eventId);
  }

  @PostMapping("/users/{userId}/events/{eventId}")
  public void insertOneIntoDatabase(@PathVariable int userId, @PathVariable int eventId) {
    wishlistService.insertOneIntoDatabase(userId, eventId);
  }

  @DeleteMapping("/users/{userId}/events/{eventId}")
  public ResponseEntity<Void> remove(@PathVariable int userId, @PathVariable int eventId) {
    boolean removed = wishlistService.remove(userId, eventId);
    if (!removed) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
