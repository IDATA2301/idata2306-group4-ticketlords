package dog.ticketlords.TicketlordsBE.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dog.ticketlords.TicketlordsBE.entity.Wishlist;
import dog.ticketlords.TicketlordsBE.repositories.WishlistRepository;

@Service
@Transactional
public class WishlistService {

  private final WishlistRepository wishlistRepository;

  public WishlistService(WishlistRepository wlRepo) {
    this.wishlistRepository = wlRepo;
  }

  @Transactional(readOnly = true)
  public List<Wishlist> getAllWishlists() {
    return wishlistRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Optional<Wishlist> getOne(Wishlist wishlist) {
    return wishlistRepository.findOne(Example.of(wishlist));
  }

  public void insertOneIntoDatabase(Wishlist wishlist) {
    wishlistRepository.save(wishlist);
  }

  public void insertManyIntoDatabase(Iterable<Wishlist> wishlists) {
    wishlistRepository.saveAll(wishlists);
  }

  public void removeWishlist(Wishlist wishlist) {
    wishlistRepository.delete(wishlist);
  }
}
