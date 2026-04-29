package dog.ticketlords.TicketlordsBE.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import dog.ticketlords.TicketlordsBE.dbentity.Event;
import dog.ticketlords.TicketlordsBE.service.EventService;
import jakarta.validation.Valid;

/**
 * REST controller for event management operations.
 * 
 * Handles GET, POST, PUT, and DELETE requests for managing events in the
 * database.
 * Provides endpoints to retrieve events by various criteria (ID, name, host,
 * category)
 * and to create, update, or delete events.
 */
@RestController
@RequestMapping("/events")
public class EventController {

  private final EventService eventService;

  /**
   * Constructs an EventController with the provided EventService.
   * 
   * @param eventService the event service to be used
   */
  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  /**
   * Retrieves all events from the database.
   * 
   * @return ResponseEntity containing a list of all events, or not found if no
   *         events exist
   */
  @GetMapping("/")
  public ResponseEntity<List<Event>> getAllEvents() {
    if (this.eventService.countAllEvents() >= 1) {
      return ResponseEntity.ok(this.eventService.getAllEvents());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Finds an event's related image.
   *
   * @param eventId the id of the event to find image from.
   * @return Http message with image, or NOT FOUND if the image wasnt found.
   */
  @GetMapping("/{eventId}/image")
  public ResponseEntity<byte[]> getImage(@PathVariable long eventId) {
    try {
      byte[] image = this.eventService.getEventImage(eventId);
      return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Retrieves popular events from the database.
   *
   * @return ResponseEntity containing a list of all events, or not found if no
   *         events exist
   */
  @GetMapping("/popular")
  public ResponseEntity<List<Event>> getPopularEvents() {
    List<Event> popularEvents = this.eventService.getPopularEvents();
    if (popularEvents.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(popularEvents);
    }
  }

  /**
   * Retrieves events based on substring in the search field from the database.
   *
   * @param query substring of the events to retrieve
   * @return ResponseEntity containing a list of all events matching the
   *         substring, or not
   *         found if no events match
   */
  @GetMapping("/search")
  public ResponseEntity<List<Event>> searchEvents(@RequestParam String query) {
    List<Event> events = this.eventService.searchEvents(query);
    if (events.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(events);
    }
  }

  /**
   * Retrieves a specific event using its ID.
   * 
   * @param eventId the ID of the event to retrieve
   * @return ResponseEntity containing the event, or not found if event does not
   *         exist
   */
  @GetMapping("/event/{eventId}")
  public ResponseEntity<Event> getEvent(@PathVariable int eventId) {
    if (this.eventService.getEvent(eventId).isPresent()) {
      return ResponseEntity.ok(eventService.getEvent(eventId).get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Retrieves all events hosted by a specific host.
   * 
   * @param hostName the name of the host
   * @return ResponseEntity containing a list of events hosted by the provided
   *         host name
   */
  @GetMapping("host/{hostName}")
  public ResponseEntity<List<Event>> getEventsByHostName(@PathVariable String hostName) {
    List<Event> events = this.eventService.getEventsByHostName(hostName);
    if (events.size() >= 1) {
      return ResponseEntity.ok(events);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Returns a list of all events containing provided event name.
   * 
   * @param eventName the name of the event
   * @return List of all events containing provided event name
   */
  @GetMapping("/name/{eventName}")
  public ResponseEntity<List<Event>> getEventsByName(@PathVariable String eventName) {
    List<Event> events = this.eventService.getEventsByName(eventName);
    if (events.size() >= 1) {
      return ResponseEntity.ok(events);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Retrieves all events in a specific category.
   * 
   * @param categoryName the name of the category
   * @return ResponseEntity containing a list of events in the provided category
   */
  @GetMapping("/category/{categoryName}")
  public ResponseEntity<List<Event>> getEventsByCategory(@PathVariable String categoryName) {
    List<Event> events = this.eventService.getEventsByCategoryName(categoryName);
    if (events.size() >= 1) {
      return ResponseEntity.ok(events);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Inserts a new event into the database.
   * 
   * @param event the event to be inserted
   * @return ResponseEntity with created status and location URI, or bad request
   *         if insertion fails
   */
  @PostMapping("/event")
  public ResponseEntity<Void> insertEventIntoDatabase(@Valid @RequestBody Event event) {
    if (this.eventService.insertEventIntoDatabase(event)) {
      return ResponseEntity.created(URI.create("/events/event/" + event.getEventId())).build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * Removes an event from the database.
   * 
   * @param eventId the ID of the event to be removed
   * @return ResponseEntity with no content status if successful, or not found if
   *         event does not exist
   */
  @DeleteMapping("/event/{eventId}")
  public ResponseEntity<Void> removeEvent(@PathVariable int eventId) {
    boolean removed = this.eventService.deleteEvent(eventId);
    if (!removed) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }

  /**
   * Updates an existing event in the database.
   * 
   * @param eventId the ID of the event to be updated
   * @param event   the updated event object
   * @return ResponseEntity with no content status if successful, or not found if
   *         event does not exist
   */
  @PutMapping("/event/{eventId}")
  public ResponseEntity<Void> updateEventInDatabase(@PathVariable int eventId, @Valid @RequestBody Event event) {
    if (this.eventService.updateEvent(eventId, event)) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

}
