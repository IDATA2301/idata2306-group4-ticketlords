package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.entity.Wishlist;
import dog.ticketlords.TicketlordsBE.entity.WishlistId;

public interface WishlistRepository extends JpaRepository<Wishlist, WishlistId> {
}
