package dog.ticketlords.TicketlordsBE.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dog.ticketlords.TicketlordsBE.DTO.TicketPurchasePayload;
import dog.ticketlords.TicketlordsBE.DTO.TicketRequestDTO;
import dog.ticketlords.TicketlordsBE.dbentity.Ticket;
import dog.ticketlords.TicketlordsBE.repositories.TicketRepository;
import jakarta.persistence.EntityExistsException;

@Service
public class TicketService {

  private final TicketRepository ticketRepo;
  private final EventService eventService;

  public TicketService(TicketRepository ticketRepo, EventService eventService) {
    this.ticketRepo = ticketRepo;
    this.eventService = eventService;
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

  /**
   * Gets a ticket by the ticket type
   *
   * @param ticketType
   * @return
   */
  public List<Ticket> getTicketsByType(String ticketType) {
    return this.ticketRepo.findByTicketTypeIgnoreCase(ticketType);
  }

  /**
   * Inserts the given {@link Ticket} into the database if no ticket with the same
   * type already exists for the event.
   *
   * @param ticketDTO the ticket data to insert
   * @return the saved ticket
   * @throws EntityExistsException if a ticket with the same type already exists
   *                               for this event
   */
  public Ticket insertTicketIntoDatabase(TicketRequestDTO ticketDTO) {
    if (ticketTypeExistsForEvent(ticketDTO.ticketType(), ticketDTO.eventId())) {
      throw new EntityExistsException(
          "A ticket with type '" + ticketDTO.ticketType() + "' already exists for event id: " + ticketDTO.eventId());
    }
    Ticket ticket = new Ticket(
        this.eventService.getEvent(ticketDTO.eventId()).get(),
        ticketDTO.ticketType(),
        ticketDTO.price(),
        ticketDTO.amountAvailable(),
        ticketDTO.ticketDescription());
    return ticketRepo.save(ticket);
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

  /**
   * Checks if a ticket type already exists for the specified event.
   *
   * @param ticketType the ticket type to check (case-insensitive)
   * @param eventId    the event id to check
   * @return {@code true} if a ticket with the given type exists for the event,
   *         {@code false} otherwise
   */
  public boolean ticketTypeExistsForEvent(String ticketType, long eventId) {
    return ticketRepo.existsByTicketTypeIgnoreCaseAndEvent_EventId(ticketType, eventId);
  }

  /**
   * Retrieves all {@link Ticket}s belonging to events with names containing the
   * given string (case-insensitive).
   *
   * @param eventName the event name substring to search for
   * @return a list of tickets for events matching the search (may be empty)
   */
  public List<Ticket> getTicketsByEventName(String eventName) {
    return ticketRepo.findAllByEvent_EventNameContainingIgnoreCase(eventName);
  }

  /**
   * Retrieves all {@link Ticket}s with a price less than or equal to the given
   * maximum value
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
   * <p>
   * The bounds are inclusive.
   *
   * @param min the minimum price (inclusive)
   * @param max the maximum price (inclusive)
   * @return a list of tickets priced between {@code min} and {@code max} (may be
   *         empty)
   */
  public List<Ticket> getTicketsInPriceRange(BigDecimal min, BigDecimal max) {
    return ticketRepo.findByPriceBetween(min, max);
  }

  /**
   * Increases the available amount of tickets for the specified ticket's id.
   *
   * @param ticketId the id to increase ticket amount for.
   * @param quantity the amount to increase.
   *
   * @return true if successful
   */
  @Transactional
  public boolean increaseAvailableTickets(long ticketId, int quantity) {
    boolean updatedRows = this.ticketRepo.increaseTicketCount(ticketId, quantity) == 1;
    if (!updatedRows) {
      throw new IllegalStateException(
          "Failed to increase quantity for ticket: ticketId=" + ticketId + ", quantity=" + quantity);
    }
    return true;
  }

  /**
   * Set a ticket's quantity in the database.
   * To be used by admins only.
   * 
   * @param ticketId the id of the ticket to change amount.
   * @param quantity the new quantity of the ticket.
   *
   * @return true if all went well.
   */
  @Transactional
  public boolean setAmountOfAvailableTickets(long ticketId, int quantity) {
    if (quantity < 0) {
      throw new IllegalArgumentException("Amount available cannot be negative: " + quantity);
    }
    return this.ticketRepo.setTicketAmount(ticketId, quantity) == 1;
  }

  /**
   * Decrements a list of multiple purchase payloads in the database. If there's
   * not enough ticket-quantity for any of the tickets that were bought,
   * all prior changed will be rollbacked.
   *
   * @param payloadList a list of transaction payloads.
   * @return true as long as all happened according to plan.
   */
  @Transactional
  public boolean decreaseAvailableTicketsByPayload(List<TicketPurchasePayload> payloadList)
      throws IllegalStateException {
    for (TicketPurchasePayload payload : payloadList) {
      boolean updatedRows = ticketRepo.reduceTicketCountIfEnough(payload.ticketId(), payload.amount()) == 1;
      if (!updatedRows) {
        throw new IllegalStateException(
            "Failed to decrement tickets: ticketId=" + payload.ticketId() + ", quantity=" + payload.amount());
      }
    }
    return true;
  }

  /**
   * Finds the amount available for a specific ticket.
   * If no ticket was found by the param there are no tickets available.
   *
   * @param ticketId the id of the ticket.
   * @return the amount available, or 0.
   */
  public int getAvailableTickets(long ticketId) {
    if (this.ticketRepo.existsById(ticketId)) {
      return this.ticketRepo.getAmountAvailable(ticketId);
    }
    return 0;
  }

}
