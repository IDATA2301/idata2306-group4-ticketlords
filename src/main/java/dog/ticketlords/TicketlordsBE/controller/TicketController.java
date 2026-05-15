package dog.ticketlords.TicketlordsBE.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dog.ticketlords.TicketlordsBE.DTO.TicketPurchasePayload;
import dog.ticketlords.TicketlordsBE.DTO.TicketRequestDTO;
import dog.ticketlords.TicketlordsBE.dbentity.Ticket;
import dog.ticketlords.TicketlordsBE.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;

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
@Tag(name = "Tickets", description = "Ticket management APIs")
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

  /**
   * Retrieves a single {@link Ticket} by its id.
   *
   * @param ticketId the ticket id
   * @return {@code 200 OK} with the ticket if found; otherwise
   *         {@code 404 Not Found}
   */
  @Operation(summary = "Get ticket by ID", description = "Returns a single ticket by its ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Ticket found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class))),
      @ApiResponse(responseCode = "404", description = "Ticket not found")
  })
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
  @Operation(summary = "Get tickets by type", description = "Returns all tickets of a specific type.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tickets found", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))),
      @ApiResponse(responseCode = "404", description = "No tickets found for the given type")
  })
  @GetMapping("/type/{ticketType}")
  public ResponseEntity<List<Ticket>> getTicketsByType(
      @Parameter(description = "Ticket type", required = true, example = "Ticket type can be: Normal - VIP or other.") @PathVariable String ticketType) {
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
  @Operation(summary = "Get tickets by event ID", description = "Returns all tickets for a specific event ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tickets found", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))),
      @ApiResponse(responseCode = "404", description = "No tickets found for the given event ID")
  })
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
  @Operation(summary = "Search tickets by event name", description = "Returns all tickets for events whose name contains the given substring (case-insensitive).")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tickets found", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))),
      @ApiResponse(responseCode = "404", description = "No tickets found for events matching the given name substring")
  })
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
  @Operation(summary = "Get tickets cheaper than a specified price", description = "Returns")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tickets found", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))),
      @ApiResponse(responseCode = "404", description = "No tickets found cheaper than the specified price")
  })
  @GetMapping("/search/price")
  public ResponseEntity<List<Ticket>> getTicketsCheaperThan(
      @Parameter(description = "Price") @RequestParam BigDecimal max) {
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
  @Operation(summary = "Get tickets in a specified price range", description = "Returns all tickets with price between the specified minimum and maximum values.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tickets found", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))),
      @ApiResponse(responseCode = "404", description = "No tickets found in the specified price range")
  })
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
   * @param ticketDTO the ticket data to be inserted
   * @return ResponseEntity with created status and location URI if successful;
   *         {@code 409 Conflict} with error message if a ticket with the same type already exists for the event;
   *         {@code 400 Bad Request} if validation fails
   */
  @Operation(summary = "Create a new ticket", description = "Inserts a new ticket into the database.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Ticket created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class))),
      @ApiResponse(responseCode = "400", description = "Invalid ticket data provided")
  })
  @PostMapping("/ticket")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> insertTicketIntoDatabase(@Valid @RequestBody TicketRequestDTO ticketDTO) {
    try {
      Ticket saved = this.ticketService.insertTicketIntoDatabase(ticketDTO);
      return ResponseEntity.created(URI.create("/tickets/" + saved.getTicketId())).build();
    } catch (EntityExistsException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
  }

  /**
   * Removes a ticket from the database.
   *
   * @param ticketId the ID of the ticket to be removed
   * @return ResponseEntity with no content status if successful, or not found if
   *         ticket does not exist
   */
  @Operation(summary = "Delete a ticket", description = "Removes a ticket from the database by its ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Ticket deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Ticket not found with the given ID")
  })
  @DeleteMapping("/ticket/{ticketId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> removeTicket(@PathVariable long ticketId) {
    boolean removed = this.ticketService.deleteTicket(ticketId);
    if (!removed) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }

  /**
   * Reduces the available ticket count for a specific ticket by the given
   * quantity if sufficient tickets are available.
   *
   * @param ticketId the ID of the ticket to purchase
   * @param quantity the number of tickets to purchase
   * @return ResponseEntity with the updated available ticket count if purchase is
   *         successful; otherwise, an internal server error response.
   */
  @Operation(summary = "Purchase tickets", description = "Reduces the available ticket count for a specific ticket by the given quantity if sufficient tickets are available.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tickets purchased successfully", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "500", description = "Failed to purchase tickets due to insufficient availability or other server error")
  })
  @PutMapping("/ticket/{ticketId}/quantity/{quantity}/purchase")
  public ResponseEntity<Map<Long, Integer>> purchaseTicketCount(@PathVariable long ticketId, @PathVariable int quantity) {
    int newAmount = this.ticketService.getAvailableTickets(ticketId) - quantity;
    if (this.ticketService.setAmountOfAvailableTickets(ticketId, newAmount)) {
      return ResponseEntity.ok(Map.of(ticketId, this.ticketService.getAvailableTickets(ticketId)));
    } else {
      return ResponseEntity.internalServerError().build();
    }

  }

  /**
   * Reduces the available ticket count for multiple tickets based on the provided
   * payload. All purchases must be successful for the operation to succeed;
   * otherwise, no changes will be made.
   *
   * @param allPurchases a list of TicketPurchasePayload objects, each containing
   *                     a ticket ID and the quantity to purchase
   * @return ResponseEntity with a map of ticket IDs to their updated available
   *         ticket counts if all purchases are successful; otherwise, an internal
   *         server error response. If the input list is empty, a bad request
   *         response will be returned.
   */
  @Operation(summary = "Purchase multiple tickets", description = "Reduces the available ticket count for multiple tickets based on the provided payload. All purchases must be successful for the operation to succeed; otherwise, no changes will be made.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tickets purchased successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketPurchasePayload.class))),
      @ApiResponse(responseCode = "400", description = "Invalid purchase payload provided"),
      @ApiResponse(responseCode = "500", description = "Failed to purchase tickets due to insufficient availability or other server error")
  })
  @PutMapping("/payload/purchase")
  public ResponseEntity<Map<Long, Integer>> purchaseMultipleTicketsCount(
      @RequestBody List<TicketPurchasePayload> allPurchases) {
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

  /**
   * For use by admins to change amount of available tickets.
   *
   * <p>
  * Example: {@code PUT /tickets/ticket/reduceAmount?ticketId=1&amp;quantity=5}
   *
   * @param ticketId the ticket id to reduce availability for
  * @param quantity the amount to reduce (must be >= 0)
   */
  @PutMapping("/ticket/reduceAmount")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> changeAmountOfAvailableTickets(
      @RequestParam long ticketId,
      @RequestParam int quantity) {
    if (quantity < 0) {
      return ResponseEntity.badRequest().build();
    }

    if (ticketService.getTicket(ticketId).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    try {
      ticketService.setAmountOfAvailableTickets(ticketId, quantity);

      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.badRequest().build();
    } catch (IllegalStateException ex) {
      // Not enough tickets available (or other failure to update)
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

}
