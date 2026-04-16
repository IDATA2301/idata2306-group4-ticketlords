package dog.ticketlords.TicketlordsBE.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.dbentity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

  List<Ticket> findByTicketTypeContainingIgnoreCase(String ticketType);

  List<Ticket> findAllByEvent_EventId(long eventId);

  List<Ticket> findByPriceLessThanEqual(BigDecimal maxPrice);

  List<Ticket> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

}

