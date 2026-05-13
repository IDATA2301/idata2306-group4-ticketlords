package dog.ticketlords.TicketlordsBE.controller;

import dog.ticketlords.TicketlordsBE.DTO.TicketPurchasePayload;
import dog.ticketlords.TicketlordsBE.dbentity.Ticket;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dog.ticketlords.TicketlordsBE.service.TicketService;
import io.micrometer.core.ipc.http.HttpSender.Response;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST controller for ticket management operations.
 * <p>
 * Handles GET, POST, PUT and DELETE requests for managing tickets in the
 * database.
 * Provides endpoints to retrieve events by various criteria (ID,....)
 * and to create, update or delete tickets.
 */
@RestController
@RequestMapping("/tickets")
public class TicketController {

  private final TicketService ticketService;

  /**
   * Constructs a TicketController with the provided TicketService.
   *
   * @param ticketService with the ticket service to be used
   */
  public TicketController(TicketService ticketService) {
    this.ticketService = ticketService;
  }
  // TODO finne ut hvilken lenke som skal brukes, og om nødvendig

  /**
   * Retrieves a single {@link Ticket} by its id.
   *
   * @param ticketId the ticket id
   * @return {@code 200 OK} with the ticket if found; otherwise
   *         {@code 404 Not Found}
   */
  @GetMapping("/{ticketId}")
  public ResponseEntity<Ticket> getTicket(@PathVariable long ticketId) {
    return ticketService.getTicket(ticketId)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Retrieves all tickets with the given ticket type (case-insensitive exact
   * match)
   *
   * <p>
   * Example: {@code GET /tickets/type/VIP}
   *
   * @param ticketType the ticket type to match (e.g., {@code VIP})
   * @return {@code 200 OK} with matching tickets; otherwise {@code 404 Not Found}
   */
  @GetMapping("/type/{ticketType}")
  public ResponseEntity<List<Ticket>> getTicketsByType(@PathVariable String ticketType) {
    List<Ticket> matches = ticketService.getTicketsByType(ticketType);
    if (matches.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(matches);
    }
  }

  /**
   * Retrieves all variations of a ticket type, by the given event's id.
   *
   * @param eventId the id of the event
   * @return List of all types of tickets relating to the event
   */
  @GetMapping("/by-event/{eventId}")
  public ResponseEntity<List<Ticket>> getTicketsByEventId(@PathVariable long eventId) {
    List<Ticket> tickets = ticketService.getTicketByEventId(eventId);
    return tickets.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(tickets);
  }

  /**
   * Searches for tickets by event name (case-insensitive substring match).
   *
   * <p>
   * Example: {@code GET /tickets/search/by-event-name?name=rock}
   *
   * @param name substring to search for in the event name
   * @return {@code 200 OK} with matching tickets; otherwise {@code 404 Not Found}
   */
  @GetMapping("/search/by-event-name")
  public ResponseEntity<List<Ticket>> getTicketsByEventName(@RequestParam String name) {
    List<Ticket> tickets = ticketService.getTicketsByEventName(name);
    if (tickets.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(tickets);
    }
  }

  /**
   * Retrieves all tickets cheaper than the specified price.
   *
   * @param max the price to filter the tickets by
   * @return a list of the tickets in the specified range
   */
  @GetMapping("/search/price")
  public ResponseEntity<List<Ticket>> getTicketsCheaperThan(@RequestParam BigDecimal max) {
    List<Ticket> tickets = ticketService.getTicketsCheaperThan(max);
    if (tickets.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(tickets);
    }
  }

  /**
   * Retrieves all tickets in the specified range.
   *
   * @param min minimum range
   * @param max maximum range
   * @return a list of the tickets in the specified range
   */
  @GetMapping("/search/price-range")
  public ResponseEntity<List<Ticket>> getTicketsInRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
    List<Ticket> tickets = ticketService.getTicketsInPriceRange(min, max);
    if (tickets.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(tickets);
    }
  }

  /**
   * Inserts a new ticket into the database.
   *
   * @param ticket the ticket to be inserted
   * @return ResponseEntity with created status and location URI, or bad request
   *         if insertion fails
   */
  @PostMapping("/ticket")
  public ResponseEntity<Void> insertTicketIntoDatabase(@Valid @RequestBody Ticket ticket) {
    Ticket saved = this.ticketService.insertTicketIntoDatabase(ticket);
    return ResponseEntity.created(URI.create("/tickets/" + saved.getTicketId())).build();
  }

  /**
   * Removes a ticket from the database.
   *
   * @param ticketId the ID of the ticket to be removed
   * @return ResponseEntity with no content status if successful, or not found if
   *         ticket does not exist
   */
  @DeleteMapping("/ticket/{ticketId}")
  public ResponseEntity<Void> removeTicket(@PathVariable long ticketId) {
    boolean removed = this.ticketService.deleteTicket(ticketId);
    if (!removed) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/ticket/{ticketId}/quantity/{quantity}/purchase")
  public ResponseEntity<Map<Long, Long>> purchaseTicketCount(@PathVariable long ticketId, @PathVariable int quantity) {
    if (this.ticketService.decreaseAvailableTickets(ticketId, quantity)) {
      return ResponseEntity.ok(Map.of(ticketId, this.ticketService.getAvailableTickets(ticketId)));
    } else {
      return ResponseEntity.internalServerError().build();
    }

  }

  @PutMapping("/payload/purchase")
  public ResponseEntity<Map<Long, Long>> purchaseMultipleTicketsCount(
      @RequestBody List<TicketPurchasePayload> allPurchases) {
    allPurchases.forEach((payload) -> System.out.println(payload.ticketId() + payload.quantity()));
    if (allPurchases.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    if (this.ticketService.decreaseAvailableTicketsByPayload(allPurchases)) {
      return ResponseEntity.ok(allPurchases.stream().collect(Collectors.toMap(
          TicketPurchasePayload::ticketId, payload -> ticketService.getAvailableTickets(payload.ticketId()))));
    } else {
      return ResponseEntity.internalServerError().build();
    }

  }

}
