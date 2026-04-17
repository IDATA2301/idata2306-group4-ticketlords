package dog.ticketlords.TicketlordsBE.controller;

import dog.ticketlords.TicketlordsBE.dbentity.Ticket;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dog.ticketlords.TicketlordsBE.service.TicketService;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

/**
 * REST controller for ticket management operations.
 * <p>
 * Handles GET, POST, PUT and DELETE requests for managing tickets in the database.
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
//TODO finne ut hvilken lenke som skal brukes, og om nødvendig
/**
 * Retrieves a specific event using its ID.
 *
 */
 @GetMapping("/{ticketId}")
 public ResponseEntity<Ticket> getTicket(@PathVariable long ticketId) {
   return ticketService.getTicket(ticketId)
       .map(ResponseEntity::ok)
       .orElseGet(() -> ResponseEntity.notFound().build());
 }


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
   * Retrieves all tickets relating to an event using event ID.
   *
   * @param eventId the id of the event
   * @return List of all the tickets relating to the event
   */
  @GetMapping("/by-event/{eventId}")
  public ResponseEntity<List<Ticket>> getTicketByEventId(@PathVariable long eventId) {
   List<Ticket> tickets = ticketService.getTicketByEventId(eventId);
   return tickets.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(tickets);
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
   * Inserts a new ticket into the database.
   *
   * @param ticket the ticket to be inserted
   * @return ResponseEntity with created status and location URI, or bad request if insertion fails
   */
  @PostMapping("/ticket")
  public ResponseEntity<Void> insertTicketIntoDatabase(@Valid @RequestBody Ticket ticket) {
    if (this.ticketService.insertTicketIntoDatabase(ticket)) {
      return ResponseEntity.created(URI.create("/tickets/ticket/" + ticket.getTicketId())).build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }


  /**
   * Removes a ticket from the database.
   *
   * @param ticketId the ID of the ticket to be removed
   * @return ResponseEntity with no content status if successful, or not found if ticket does not exist
   */
  @DeleteMapping("/ticket/{ticketId}")
  public ResponseEntity<Void> removeTicket(@PathVariable int ticketId) {
    boolean removed = this.ticketService.deleteTicket(ticketId);
    if (!removed) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }


}
