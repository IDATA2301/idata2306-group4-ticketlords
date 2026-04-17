package dog.ticketlords.TicketlordsBE.service;

import dog.ticketlords.TicketlordsBE.dbentity.Ticket;
import dog.ticketlords.TicketlordsBE.repositories.TicketRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

  private final TicketRepository ticketRepo;

  public TicketService(TicketRepository ticketRepo) {
    this.ticketRepo = ticketRepo;
  }

  /**
   * Gets a ticket by the ticket id.
   *
   * @param ticketId the id of the ticket.
   * @return the ticket whose id corresponds with the param ticketId.
   */
  public Optional<Ticket> getTicket(long ticketId) {
    return this.ticketRepo.findById(ticketId);
  }

  public List<Ticket> getTicketsByType(String ticketType) {
    return this.ticketRepo.findByTicketTypeIgnoreCase(ticketType);
  }

  /**
   *
   * @param ticket
   * @return
   */
  public boolean insertTicketIntoDatabase(Ticket ticket) {
    if (!this.ticketRepo.existsById(ticket.getTicketId())) {
      this.ticketRepo.save(ticket);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Deletes a {@link Ticket} column in the database if it exists.
   *
   * @param ticketId the id of the ticket to delete.
   *
   * @return true if the deletion succeeded, false otherwise.
   */
  public boolean deleteTicket(long ticketId) {
    if (this.ticketRepo.existsById(ticketId)) {
      this.ticketRepo.deleteById(ticketId);
      return true;
    }
    return false;
  }

  /**
   * Retrieves all {@link Ticket}s belonging to the given event.
   *
   * @param eventId the event id to filter tickets by
   * @return a list of tickets for the specified event (may be empty)
   */
  public List<Ticket> getTicketByEventId(long eventId) {
    return ticketRepo.findAllByEvent_EventId(eventId);
  }

  public List<Ticket> getTicketsByEventName(String eventName) {
    return ticketRepo.findAllByEvent_EventNameContainingIgnoreCase(eventName);
  }


  /**
   * Retrieves all {@link Ticket}s with a price less than or equal to the given maximum value
   *
   * @param maxPrice the maximum price (inclusive)
   * @return a list of tickets priced at most {@code maxPrice} (may be empty)
   */
  public List<Ticket> getTicketsCheaperThan(BigDecimal maxPrice) {
    return ticketRepo.findByPriceLessThanEqual(maxPrice);
  }

  /**
   * Retrieves all {@link Ticket}s with a price within the given range.
   *
   * <p>The bounds are inclusive.
   *
   * @param min the minimum price (inclusive)
   * @param max the maximum price (inclusive)
   * @return a list of tickets priced between {@code min} and {@code max} (may be empty)
   */
  public List<Ticket> getTicketsInPriceRange(BigDecimal min, BigDecimal max) {
    return ticketRepo.findByPriceBetween(min, max);
  }

}
