package dog.ticketlords.TicketlordsBE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import dog.ticketlords.TicketlordsBE.repositories.WishlistRepository;

public class WishlistController {

  @Autowired
  private WishlistRepository wishlistRepository;

  public WishlistController() {

  }

  // TODO: Remove later, just to demonstrate.
  public void test() {
    wishlistRepository.count();

  }
}
