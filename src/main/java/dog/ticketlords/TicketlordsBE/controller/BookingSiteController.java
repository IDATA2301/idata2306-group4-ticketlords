package dog.ticketlords.TicketlordsBE.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dog.ticketlords.TicketlordsBE.dbentity.BookingSite;
import dog.ticketlords.TicketlordsBE.service.BookingSiteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for booking site management operations.
 *
 * Handles GET, POST, PUT, and DELETE requests for managing booking sites in the
 * database.
 * Provides endpoints to retrieve booking sites by various criteria (ID, name)
 * and to create, update, or delete booking sites.
 */
@Tag(name = "Booking Sites", description = "APIs for managing booking sites. Unused functionality, and is therefore deprecated.")
@RestController
@RequestMapping("/booking-sites")
public class BookingSiteController {

  private final BookingSiteService bookingSiteService;

  /**
   * Constructs a BookingSiteController with the provided BookingSiteService.
   *
   * @param bookingSiteService the booking site service to be used
   */
  public BookingSiteController(BookingSiteService bookingSiteService) {
    this.bookingSiteService = bookingSiteService;
  }

  /**
   * Retrieves all booking sites from the database.
   *
   * @return ResponseEntity containing a list of all booking sites, or not found
   *         if no booking sites exist.
   */
  @Operation(summary = "Get all booking sites", description = "Retrieves a list of all booking sites available in the database.", deprecated = true)
  @GetMapping("/")
  public ResponseEntity<List<BookingSite>> getAllBookingSites() {
    List<BookingSite> bookingSites = this.bookingSiteService.getAllBookingSites();
    return bookingSites.isEmpty()
        ? ResponseEntity.notFound().build()
        : ResponseEntity.ok(bookingSites);
  }

  /**
   * Retrieves a specific booking site using its ID.
   *
   * @param bookingSiteId the ID of the booking site to retrieve
   * @return ResponseEntity containing the booking site, or not found if booking
   *         site does not exist
   */
  @Operation(summary = "Get booking site by ID", description = "Retrieves a booking site by its unique ID.", deprecated = true)
  @GetMapping("/{bookingSiteId}")
  public ResponseEntity<BookingSite> getBookingSiteById(@PathVariable long bookingSiteId) {
    Optional<BookingSite> bookingSite = this.bookingSiteService.getBookingSiteById(bookingSiteId);
    return bookingSite.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Retrieves a booking site by its name.
   *
   * @param siteName the name of the booking site
   * @return ResponseEntity containing the booking site, or not found if booking
   *         site does not exist
   */
  @Operation(summary = "Get booking site by name", description = "Retrieves a booking site by its name.", deprecated = true)
  @GetMapping("/name/{siteName}")
  public ResponseEntity<BookingSite> getBookingSiteByName(@PathVariable String siteName) {
    Optional<BookingSite> bookingSite = this.bookingSiteService.getBookingSiteByName(siteName);
    return bookingSite.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Searches for booking sites by name (case\-insensitive substring match).
   *
   * @param name substring to search for in the booking site name
   * @return ResponseEntity containing matching booking sites, or not found if
   *         none match
   */
  @Operation(summary = "Search booking sites by name", description = "Searches for booking sites whose names contain the specified substring (case-insensitive).", deprecated = true)
  @GetMapping("/search")
  public ResponseEntity<List<BookingSite>> search(@RequestParam String name) {
    List<BookingSite> matches = this.bookingSiteService.getBookingSitesByNameContaining(name);
    return matches.isEmpty()
        ? ResponseEntity.notFound().build()
        : ResponseEntity.ok(matches);
  }

  /**
   * Inserts a new booking site into the database.
   *
   * @param bookingSite the booking site to be inserted
   * @return ResponseEntity with created status and location URI, or bad request
   *         if insertion fails
   */
  @Operation(summary = "Create a new booking site", description = "Inserts a new booking site into the database. The request body must contain a valid BookingSite object. Returns the location of the created booking site in the response header if successful.", deprecated = true)
  @PostMapping("/booking-site")
  public ResponseEntity<Void> insertBookingSite(@Valid @RequestBody BookingSite bookingSite) {
    if (this.bookingSiteService.insertBookingSiteToDatabase(bookingSite)) {
      return ResponseEntity.created(URI.create("/booking-sites/" + bookingSite.getTicketVendorId())).build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * Updates an existing booking site in the database.
   *
   * @param bookingSite the updated booking site object
   * @return ResponseEntity with no content status if successful, or not found if
   *         booking site does not exist
   */
  @Operation(summary = "Update an existing booking site", description = "Updates an existing booking site in the database. The request body must contain a valid BookingSite object with an existing ID. Returns no content if the update is successful, or not found if the booking site does not exist.", deprecated = true)
  @PutMapping("/booking-site/{bookingSiteId}")
  public ResponseEntity<Void> updateBookingSite(@Valid @RequestBody BookingSite bookingSite) {
    if (this.bookingSiteService.updateBookingSite(bookingSite)) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Removes a booking site from the database.
   *
   * @param bookingSiteId the ID of the booking site to be removed
   * @return ResponseEntity with no content status if successful, or not found if
   *         booking site does not exist
   */
  @Operation(summary = "Delete a booking site", description = "Deletes a booking site from the database by its ID. Returns no content if the deletion is successful, or not found if the booking site does not exist.", deprecated = true)
  @DeleteMapping("/booking-site/{bookingSiteId}")
  public ResponseEntity<Void> deleteBookingSite(@PathVariable long bookingSiteId) {
    if (this.bookingSiteService.deleteBookingSite(bookingSiteId)) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
