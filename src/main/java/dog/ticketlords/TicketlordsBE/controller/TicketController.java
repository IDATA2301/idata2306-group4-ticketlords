package dog.ticketlords.TicketlordsBE.controller;

import dog.ticketlords.TicketlordsBE.dbentity.Ticket;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dog.ticketlords.TicketlordsBE.service.TicketService;

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
    List<Ticket> matches = ticketService.getTicketType(ticketType);
    return matches.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(matches);
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

}
