package dog.ticketlords.TicketlordsBE.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dog.ticketlords.TicketlordsBE.dbentity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

  List<Ticket> findByTicketTypeIgnoreCase(String ticketType);

  List<Ticket> findAllByEvent_EventId(long eventId);

  List<Ticket> findAllByEvent_EventNameContainingIgnoreCase(String eventName);

  List<Ticket> findByPriceLessThanEqual(BigDecimal maxPrice);

  List<Ticket> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

  boolean existsByTicketTypeIgnoreCaseAndEvent_EventId(String ticketType, long eventId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("UPDATE Ticket t " +
      "SET t.amountAvailable = t.amountAvailable - :ticketAmount " +
      "WHERE t.ticketId = :ticketId AND t.amountAvailable >= :ticketAmount")
  int reduceTicketCountIfEnough(@Param("ticketId") long ticketId, @Param("ticketAmount") int ticketAmount);

  @Modifying
  @Query("UPDATE Ticket t " +
      "SET t.amountAvailable = t.amountAvailable + :ticketAmount " +
      "WHERE t.ticketId = :ticketId")
  int increaseTicketCount(@Param("ticketId") long ticketId, @Param("ticketAmount") int ticketAmount);

  @Modifying
  @Query("UPDATE Ticket t " +
      "SET t.amountAvailable = :amount " +
      "WHERE t.ticketId = :ticketId")
  int setTicketAmount(@Param("ticketId") long ticketId, @Param("amount") int ticketAmount);
}
