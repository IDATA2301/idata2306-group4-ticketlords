package dog.ticketlords.TicketlordsBE.service;

import java.util.Optional;

import org.springframework.data.domain.Example;

import dog.ticketlords.TicketlordsBE.entity.Wishlist;
import dog.ticketlords.TicketlordsBE.repositories.WishlistRepository;

public class WishlistService {

  private WishlistRepository wishlistRepository;

  public WishlistService(WishlistRepository wlRepo) {
    this.wishlistRepository = wlRepo;
  }

  public Iterable<Wishlist> getAll() {
    return wishlistRepository.findAll();
  }

  public Optional<Wishlist> getOne(Wishlist wishlist) {
    return wishlistRepository.findOne(Example.of(wishlist));
  }

  public void insertOneIntoDatabase(Wishlist wishlist) {
    wishlistRepository.save(wishlist);
  }

  public void insertManyIntoDatabase(Iterable<Wishlist> wishlists) {
    wishlistRepository.saveAll(wishlists);
  }
}
