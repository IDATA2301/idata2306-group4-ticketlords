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
public class WishlistController {

  @Autowired
  private WishlistRepository wishlistRepository;

  private WishlistService wishlistService;

  public WishlistController(WishlistService wlService) {
    this.wishlistService = wlService;
  }

}
