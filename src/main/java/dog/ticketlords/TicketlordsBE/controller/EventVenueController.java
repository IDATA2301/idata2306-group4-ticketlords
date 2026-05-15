package dog.ticketlords.TicketlordsBE.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
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

import dog.ticketlords.TicketlordsBE.dbentity.EventVenue;
import dog.ticketlords.TicketlordsBE.service.EventVenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/venues")
@Tag(name = "Event Venues", description = "APIs for managing event venues, including retrieval, addition, update, and deletion of venues.")
public class EventVenueController {

  private EventVenueService eventVenueService;

  public EventVenueController(EventVenueService eventVenueService) {
    this.eventVenueService = eventVenueService;
  }

  /**
   * Retrieves a venue by its unique id.
   *
   * @param id the id of the venue
   * @return ResponseEntity containing the EventVenue if found, or 404 if not
   *         found
   */
  @GetMapping("/{id}")
  @Operation(summary = "Get a venue by its unique id", description = "Retrieves a venue by its unique id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Venue found and returned successfully"),
      @ApiResponse(responseCode = "404", description = "Venue not found")
  })
  @ApiResponse(responseCode = "200", description = "Venue found and returned successfully")
  public ResponseEntity<EventVenue> getVenueById(@PathVariable long id) {
    Optional<EventVenue> venue = this.eventVenueService.getEventVenueById(id);
    return venue.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Retrieves a list of venues by country and city.
   *
   * @param country the country of the venues
   * @param city    the city of the venues
   * @return ResponseEntity containing a list of EventVenues
   */
  @Operation(summary = "Get venues by location", description = "Retrieves a list of venues by country and city")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Venues found and returned successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid country or city parameters")
  })
  @GetMapping("/location")
  public ResponseEntity<List<EventVenue>> getVenuesByLocation(
      @Parameter(description = "Name of the country", required = true, example = "/venues/location?country='country name") @RequestParam String country,
      @Parameter(description = "Name of the city", required = true, example = "/venues/location?country='country name&city='city name'") @RequestParam String city) {
    List<EventVenue> venues = this.eventVenueService.getEventVenueByLocation(country, city);
    return ResponseEntity.ok(venues);
  }

  /**
   * Searches event venues by any subset of address, arena, city and country.
   *
   * All parameters are optional. Null/blank parameters are ignored.
   *
   * @param address the address of the venue
   * @param arena   the arena of the venue
   * @param city    the city of the venue
   * @param country the country of the venue
   * @return a list of matching venues, or 204 if none
   */
  @GetMapping("/search")
  public ResponseEntity<List<EventVenue>> searchVenues(
      @RequestParam(required = false) String address,
      @RequestParam(required = false) String arena,
      @RequestParam(required = false) String city,
      @RequestParam(required = false) String country) {

    List<EventVenue> venues = this.eventVenueService.getEventVenueByAnySubset(address, arena, city, country);
    if (venues.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(venues);
  }


  /**
   * Adds a new venue to the database.
   *
   * @param eventVenue the EventVenue object to add
   * @return ResponseEntity with status 201 if created, 409 if already exists
   */
  @Operation(summary = "Add a new venue", description = "Adds a new venue to the database")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Venue added successfully"),
      @ApiResponse(responseCode = "409", description = "Venue already exists")
  })
  @PostMapping("/add")
  public ResponseEntity<String> addVenue(@RequestBody EventVenue eventVenue) {
    boolean added = this.eventVenueService.addEventVenueToDatabase(eventVenue);
    if (added) {
      return ResponseEntity.status(HttpStatus.CREATED).body("Venue added successfully");
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Venue already exists");
    }
  }

  /**
   * Retrieves a venue by its address.
   *
   * @param address the address of the venue
   * @return ResponseEntity containing the EventVenue if found, or 404 if not
   *         found
   */
  @Operation(summary = "Get a venue by its address", description = "Retrieves a venue by its address")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Venue found and returned successfully"),
      @ApiResponse(responseCode = "404", description = "Venue not found")
  })
  @GetMapping("/address")
  public ResponseEntity<List<EventVenue>> getVenueByAddress(@RequestParam String address) {
    return ResponseEntity.ok(this.eventVenueService.getVenueByAddress(address));
  }

  /**
   * Updates an existing venue.
   *
   * @param venueId      the id of the venue to update
   * @param updatedVenue the updated EventVenue object
   * @return ResponseEntity with status 200 if updated, 404 if not found
   */

  @Operation(summary = "Update an existing venue", description = "Updates an existing venue by its id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Venue updated successfully"),
      @ApiResponse(responseCode = "404", description = "Venue not found")
  })
  @PutMapping("/update/{id}")
  public ResponseEntity<String> updateVenue(@PathVariable long venueId, @RequestBody EventVenue updatedVenue) {
    boolean updated = this.eventVenueService.updateEventVenue(venueId, updatedVenue);
    if (updated) {
      return ResponseEntity.ok("Venue updated successfully");
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Deletes a venue by its id.
   *
   * @param id the id of the venue to delete
   * @return ResponseEntity with status 200 if deleted, 404 if not found
   */
  @Operation(summary = "Delete a venue by id", description = "Deletes a venue by its unique id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Venue deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Venue not found")
  })
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteVenue(@PathVariable long id) {
    boolean deleted = this.eventVenueService.deleteEventVenueById(id);
    if (deleted) {
      return ResponseEntity.ok("Venue deleted successfully");
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Counts the total number of venues.
   *
   * @return ResponseEntity containing the count of venues
   */
  @Operation(summary = "Count total venues", description = "Counts the total number of venues in the database")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
  })
  @GetMapping("/count")
  public ResponseEntity<Long> countVenues() {
    long count = this.eventVenueService.countEventVenues();
    return ResponseEntity.ok(count);
  }
}
