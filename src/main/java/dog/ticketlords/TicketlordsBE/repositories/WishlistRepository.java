package dog.ticketlords.TicketlordsBE.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.dbentity.Wishlist;
import dog.ticketlords.TicketlordsBE.dbentity.WishlistId;

public interface WishlistRepository extends JpaRepository<Wishlist, WishlistId> {

  List<Wishlist> findAllByUser_UserId(long userId);

  Page<Wishlist> findAllByUser_UserId(long userId, Pageable pageable);

  long countByEvent_EventId(long eventId);

  long countByRegisteredUser_UserId(long userId);

  long countByEvent_EventNameContainingIgnoreCase(String eventNameSubstring);

}
