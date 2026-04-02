package dog.ticketlords.TicketlordsBE.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dog.ticketlords.TicketlordsBE.entity.RegisteredUser;
import dog.ticketlords.TicketlordsBE.entity.Review;
import dog.ticketlords.TicketlordsBE.entity.Wishlist;
import dog.ticketlords.TicketlordsBE.entity.WishlistId;
import dog.ticketlords.TicketlordsBE.repositories.WishlistRepository;

/**
 * Service to handle operations directly with database concerning {@link Wishlist}.
 * Includes methods like getting, inserting and removing wishes from wishlist.
 */
@Service
@Transactional
public class WishlistService {

  private final WishlistRepository wishlistRepository;

  /**
   * Creates a new service for wishlist operations.
   *
   * @param wlRepo repository used to perform persistence operations for {@link Wishlist}
   */
  public WishlistService(WishlistRepository wlRepo) {
    this.wishlistRepository = wlRepo;
  }

  /**
   * Returns every wishlist in the database.
   * @return a list of all wishlists
   */
  @Transactional(readOnly = true)
  public List<Wishlist> getAllWishlists() {
    return this.wishlistRepository.findAll();
  }

  /**
   * Returns a singular wish using userId and eventId
   * @param userId the primary key of the user
   * @param eventId the primary key of the event
   * @return the object of the wish as an optional.
   */
  @Transactional(readOnly = true)
  public Optional<Wishlist> getWish(int userId, int eventId) {
    return this.wishlistRepository.findById(new WishlistId(userId, eventId));
  }

  /**
   * Insert a singular wish into the database
   * @param wish the wish to add
   */
  public void insertOneIntoDatabase(int userId, int eventId) {
    this.wishlistRepository.save();
  }

  /** TODO: unsure if needed
   * Inserts multiple wishlists into the database
   * 
   * @param wishlists the iterator for wishlists
   */
  public void insertManyIntoDatabase(Iterable<Wishlist> wishlists) {
    this.wishlistRepository.saveAll(wishlists);
  }

  /**
   * Removes a singular wish
   * 
   * @param wish the wish to remove
   */
  public void removeWishlist(Wishlist wish) {
    this.wishlistRepository.delete(wish);
  }
}
