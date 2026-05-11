package dog.ticketlords.TicketlordsBE.controller;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
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
import org.springframework.web.multipart.MultipartFile;

import dog.ticketlords.TicketlordsBE.DTO.EventRequestDTO;
import dog.ticketlords.TicketlordsBE.dbentity.Category;
import dog.ticketlords.TicketlordsBE.dbentity.Event;
import dog.ticketlords.TicketlordsBE.dbentity.EventVenue;
import dog.ticketlords.TicketlordsBE.service.CategoryService;
import dog.ticketlords.TicketlordsBE.service.EventService;
import dog.ticketlords.TicketlordsBE.service.EventVenueService;
import dog.ticketlords.TicketlordsBE.service.ImageStorageService;
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
  private final CategoryService categoryService;
  private final EventVenueService eventVenueService;
  private final ImageStorageService imageStorageService;

  /**
   * Constructs an EventController with the provided services.
   * 
   * @param eventService the event service to be used
   * @param categoryService the category service to be used
   * @param eventVenueService the event venue service to be used
   * @param imageStorageService the image storage service to be used
   */
  public EventController(EventService eventService, CategoryService categoryService,
          EventVenueService eventVenueService, ImageStorageService imageStorageService) {
    this.eventService = eventService;
    this.categoryService = categoryService;
    this.eventVenueService = eventVenueService;
    this.imageStorageService = imageStorageService;
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
   * Finds an event's related image's name.
   *
   * @param eventId the id of the event to find image-name from.
   * @return HTTP redirect to static folder location if found, NOT FOUND
   *         otherwise.
   */
  @GetMapping("/{eventId}/image")
  public ResponseEntity<Void> getImageUrl(@PathVariable long eventId) {
    try {
      String fileName = this.eventService.getEventImageName(eventId);
      return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/images/" + fileName).build();
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
   * Uploads an image to storage to be used.
   * 
   * @return ResponseEntity containing url to the image that was posted
   */
  @PostMapping("/upload-image")
  public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
    try {
      String key = imageStorageService.upload(file);
      String url = "" + key;
      return ResponseEntity.ok(url);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Upload failed: " + e.getMessage());
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
  public ResponseEntity<String> insertEventIntoDatabase(@Valid @RequestBody EventRequestDTO eventDTO) {
    Optional<Category> category = this.categoryService.getCategoryByCategoryId(eventDTO.categoryId());
    Optional<EventVenue> eventVenue = this.eventVenueService.getEventVenueById(eventDTO.venueId());

    if (category.isEmpty() || eventVenue.isEmpty()) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
                  .body("Error adding category and/or eventVenue");
    }

    // Creating the actual Event
    Event event = new Event(
                    eventDTO.eventName(),
                    eventDTO.host(),
                    category.get(),
                    eventVenue.get(),
                    eventDTO.eventDescription(),
                    0,
                    eventDTO.eventDateStart(),
                    eventDTO.eventDateEnd(),
                    eventDTO.imgPathUrl()
    );

    Optional<Event> saved = this.eventService.insertEventIntoDatabase(event);
    if (saved.isPresent()) {
      return ResponseEntity.created(URI.create("/events/event/" + saved.get().getEventId())).build();
    } else {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
