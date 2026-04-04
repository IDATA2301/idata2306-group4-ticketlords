package dog.ticketlords.TicketlordsBE.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dog.ticketlords.TicketlordsBE.entity.Event;
import dog.ticketlords.TicketlordsBE.entity.RegisteredUser;
import dog.ticketlords.TicketlordsBE.entity.Review;
import dog.ticketlords.TicketlordsBE.entity.Wishlist;
import dog.ticketlords.TicketlordsBE.entity.WishlistId;
import dog.ticketlords.TicketlordsBE.repositories.EventRepository;
import dog.ticketlords.TicketlordsBE.repositories.RegisteredUserRepository;
import dog.ticketlords.TicketlordsBE.repositories.WishlistRepository;

/**
 * Service to handle operations directly with database concerning
 * {@link Wishlist}.
 * Includes methods like getting, inserting and removing wishes from wishlist.
 */
@Service
@Transactional
public class WishlistService {

  private final WishlistRepository wishlistRepository;
  private final EventRepository eventRepository;
  private final RegisteredUserRepository registeredUserRepository;

  /**
   * Creates a new service for wishlist operations.
   *
   * @param wlRepo repository used to perform persistence operations for
   *               {@link Wishlist}
   */
  public WishlistService(WishlistRepository wlRepo, EventRepository eRepo, RegisteredUserRepository uRepo) {
    this.wishlistRepository = wlRepo;
    this.eventRepository = eRepo;
    this.registeredUserRepository = uRepo;
  }

  /**
   * Returns every wishlist in the database.
   * 
   * @return a list of all wishlists
   */
  @Transactional(readOnly = true)
  public List<Wishlist> getAllWishlists() {
    return this.wishlistRepository.findAll();
  }

  /**
   * Returns a singular wish using userId and eventId
   * 
   * @param userId  the primary key of the user
   * @param eventId the primary key of the event
   * @return the object of the wish as an optional.
   */
  @Transactional(readOnly = true)
  public Optional<Wishlist> getWish(int userId, int eventId) {
    return this.wishlistRepository.findById(new WishlistId(userId, eventId));
  }

  public List<Wishlist> getAllUsersWishes(long userId) {
    return this.wishlistRepository.findAllByIdUserId(userId);
  }

  /**
   * Insert a singular wish into the database
   * 
   * @param userId  the id of the user who makes the wishlist
   * @param eventId the id of the wish, that the user wants to save
   */
  public boolean insertOneIntoDatabase(long userId, long eventId) {
    Optional<RegisteredUser> user = registeredUserRepository.findById(userId);
    Optional<Event> event = eventRepository.findById(eventId);
    if (user.isPresent() && event.isPresent()) {
      Wishlist wishlist = new Wishlist(user.get(), event.get());
      this.wishlistRepository.save(wishlist);
      return true;
    } else {
      return false;
    }
  }

  /**
   * TODO: unsure if needed
   * Inserts multiple wishlists into the database
   * 
   * @param wishlists the iterator for wishlists
   */
  public void insertManyIntoDatabase(Iterable<Wishlist> wishlists) {
    this.wishlistRepository.saveAll(wishlists);
  }

  /**
   * Removes a singular wish, by its embedded primary-key values.
   * 
   * @param userId     the id of the user who'se wish is to be deleted.
   * @param wishlistId the id of the wishlist to be deleted.
   */
  public boolean removeWishlist(long userId, long eventId) {
    WishlistId id = new WishlistId(userId, eventId);
    if (wishlistRepository.existsById(id)) {
      wishlistRepository.deleteById(id);
      return true;
    } else {
      return false;
    }
  }
}
