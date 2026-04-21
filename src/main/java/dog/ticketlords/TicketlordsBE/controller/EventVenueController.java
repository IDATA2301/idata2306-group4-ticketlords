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

@RestController
@RequestMapping("/venues")
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
  @GetMapping("/location")
  public ResponseEntity<List<EventVenue>> getVenuesByLocation(@RequestParam String country, @RequestParam String city) {
    List<EventVenue> venues = this.eventVenueService.getEventVenueByLocation(country, city);
    return ResponseEntity.ok(venues);
  }

  /**
   * Adds a new venue to the database.
   *
   * @param eventVenue the EventVenue object to add
   * @return ResponseEntity with status 201 if created, 409 if already exists
   */
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
  @GetMapping("/address")
  public ResponseEntity<EventVenue> getVenueByAddress(@RequestParam String address) {
    Optional<EventVenue> venue = this.eventVenueService.getVenueByAddress(address);
    return venue.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Updates an existing venue.
   *
   * @param venueId      the id of the venue to update
   * @param updatedVenue the updated EventVenue object
   * @return ResponseEntity with status 200 if updated, 404 if not found
   */
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
  @GetMapping("/count")
  public ResponseEntity<Long> countVenues() {
    long count = this.eventVenueService.countEventVenues();
    return ResponseEntity.ok(count);
  }
}
