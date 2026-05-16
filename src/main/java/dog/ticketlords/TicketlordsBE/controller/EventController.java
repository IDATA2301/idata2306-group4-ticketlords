package dog.ticketlords.TicketlordsBE.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.security.core.Authentication;

import dog.ticketlords.TicketlordsBE.exception.ResourceNotFoundException;
import dog.ticketlords.TicketlordsBE.service.ImageStorageService;
import dog.ticketlords.TicketlordsBE.service.EventService;
import dog.ticketlords.TicketlordsBE.DTO.EventRequestDTO;
import dog.ticketlords.TicketlordsBE.dbentity.Event;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Events", description = "Event management APIs")
public class EventController {

  private final EventService eventService;
  private final ImageStorageService imageStorageService;

  /**
   * Constructs an EventController with the provided services.
   * 
   * @param eventService        the event service to be used
   * @param categoryService     the category service to be used
   * @param eventVenueService   the event venue service to be used
   * @param imageStorageService the image storage service to be used
   */
  public EventController(EventService eventService, ImageStorageService imageStorageService) {
    this.eventService = eventService;
    this.imageStorageService = imageStorageService;
  }

  /**
   * Retrieves all events from the database.
   * Non-admin users only see publicly visible events.
   * 
   * @return ResponseEntity containing a list of all events (filtered by visibility for non-admins),
   *         or not found if no events exist
   */
  @Operation(summary = "Get all events", description = "Retrieves a list of all events available in the database. Non-admin users only see publicly visible events.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Events retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class, type = "array", description = "A list of events retrieved from the database."))),
      @ApiResponse(responseCode = "404", description = "No publicly visible events found in the database", content = @Content)
  })
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
   * @return HTTP redirect to a short-lived presigned URL if found, NOT FOUND otherwise.
   */
  @Operation(summary = "Get event image URL", description = "Retrieves the URL of the image associated with a specific event.")
  @ApiResponses({
      @ApiResponse(responseCode = "302", description = "Image URL retrieved successfully, redirecting to the image location.", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", format = "uri", description = "The URL of the image associated with the specified event.", example = "/images/event123.jpg"))),
      @ApiResponse(responseCode = "404", description = "Event not found or no image associated with the event.", content = @Content)
  })
  @GetMapping("/{eventId}/image")
  public ResponseEntity<Void> getImageUrl(@PathVariable long eventId) {
    try {
      String objectKey = this.eventService.getEventImageName(eventId);
      String presignedUrl = this.imageStorageService.getPresignedUrl(objectKey);
      return ResponseEntity.status(HttpStatus.FOUND)
          .header(HttpHeaders.LOCATION, presignedUrl)
          .build();
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  /**
   * Retrieves popular events from the database.
   * Non-admin users only see publicly visible events.
   *
   * @return ResponseEntity containing a list of the top 10 events based on click count
   *         (filtered by visibility for non-admins), or not found if no events exist
   */
  @Operation(summary = "Get popular events", description = "Retrieves a list of popular the top 10 events based on click count. Non-admin users only see publicly visible events.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Popular events retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class, type = "array", description = "A list of popular events retrieved from the database."))),
      @ApiResponse(responseCode = "404", description = "No popular publicly visible events found in the database", content = @Content)
  })
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
   * Non-admin users only see publicly visible events.
   *
   * @param query substring of the events to retrieve
   * @return ResponseEntity containing a list of all events matching the
   *         substring (filtered by visibility for non-admins), or not
   *         found if no events match
   */
  @Operation(summary = "Search events", description = "Retrieves a list of events that match the provided search query. The search looks for matches in the event name, host, and category name, and is case-insensitive. Non-admin users only see publicly visible events.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Events matching the search query retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class, type = "array", description = "A list of events that match the provided search query."))),
      @ApiResponse(responseCode = "404", description = "No publicly visible events found matching the search query", content = @Content)
  })
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
  @Operation(summary = "Get event by ID", description = "Returns the event with the given ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Event found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class))),
      @ApiResponse(responseCode = "404", description = "Event not found")
  })
  @GetMapping("/event/{eventId}")
public ResponseEntity<Event> getEvent(@PathVariable int eventId) {
    Optional<Event> event = this.eventService.getEvent(eventId);
    
    if (event.isEmpty()) {
        return ResponseEntity.notFound().build();
    }
    
    // Check visibility for non-admins
    Event e = event.get();
    if (!e.isPubliclyVisible() && !isCurrentUserAdmin()) {
        return ResponseEntity.notFound().build();
    }
    
    return ResponseEntity.ok(e);
}

  /**
   * Retrieves all events hosted by a specific host.
   * Non-admin users only see publicly visible events.
   * 
   * @param hostName the name of the host
   * @return ResponseEntity containing a list of events hosted by the provided
   *         host name (filtered by visibility for non-admins)
   */
  @Operation(summary = "Get events by host name", description = "Returns a list of all events hosted by the specified host name. Non-admin users only see publicly visible events.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Events found for the specified host name", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class, type = "array", description = "A list of events hosted by the specified host name."))),
      @ApiResponse(responseCode = "404", description = "No publicly visible events found for the specified host name")
  })
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
   * Non-admin users only see publicly visible events.
   * 
   * @param eventName the name of the event
   * @return List of all events containing provided event name (filtered by visibility for non-admins)
   */
  @Operation(summary = "Get events by name", description = "Returns a list of all events whose names contain the specified substring (case-insensitive). Non-admin users only see publicly visible events.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Events found matching the specified name substring", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class, type = "array", description = "A list of events whose names contain the specified substring."))),
      @ApiResponse(responseCode = "404", description = "No publicly visible events found matching the specified name substring")
  })
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
   * Non-admin users only see publicly visible events.
   * 
   * @param categoryName the name of the category
   * @return ResponseEntity containing a list of events in the provided category
   *         (filtered by visibility for non-admins)
   */
  @Operation(summary = "Get events by category name", description = "Returns a list of all events that belong to the specified category name (case-insensitive). Non-admin users only see publicly visible events.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Events found for the specified category name", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class, type = "array", description = "A list of events that belong to the specified category name."))),
      @ApiResponse(responseCode = "404", description = "No publicly visible events found for the specified category name")
  })
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
  @Operation(summary = "Upload event image", description = "Uploads an image file to the server and returns the URL where the image can be accessed. The uploaded image can then be associated with an event by using the returned URL in the event's imgPathUrl field.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Image uploaded successfully", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", format = "uri", description = "The URL where the uploaded image can be accessed.", example = "images/2625f221-873a-462e-a511-a89f99f94ce8-image.jpg"))),
      @ApiResponse(responseCode = "500", description = "Image upload failed due to an internal server error", content = @Content)
  })
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
  @Operation(summary = "Create a new event", description = "Inserts a new event into the database using the provided event details. The request body should contain all necessary information for creating the event, including the event name, host, category ID, venue ID, description, start and end dates, and an optional image URL. If the specified category or venue does not exist, the request will fail with a conflict status.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Event created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class, description = "The event that was created in the database."))),
      @ApiResponse(responseCode = "400", description = "Invalid event data provided in the request body", content = @Content),
      @ApiResponse(responseCode = "409", description = "Specified category or venue does not exist", content = @Content)
  })
  @PostMapping("/event")
  public ResponseEntity<Void> insertEventIntoDatabase(@Valid @RequestBody EventRequestDTO eventDTO) {
    Optional<Event> saved = this.eventService.insertEventIntoDatabase(eventDTO);
    if (saved.isPresent()) {
      return ResponseEntity.created(URI.create("/event/" + saved.get().getEventId())).build();
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
  @Operation(summary = "Delete an event", description = "Removes an event from the database using its ID. If the event with the specified ID does not exist, the request will return a not found status.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Event deleted successfully, no content returned.", content = @Content),
      @ApiResponse(responseCode = "404", description = "Event not found with the specified ID, no deletion performed.", content = @Content)
  })
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
  @Operation(summary = "Update an event", description = "Updates an existing event in the database using its ID and the provided updated event details. The request body should contain all necessary information for updating the event, including the event name, host, category ID, venue ID, description, start and end dates, and an optional image URL. If the event with the specified ID does not exist, the request will return a not found status.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Event updated successfully, no content returned.", content = @Content),
      @ApiResponse(responseCode = "404", description = "Event not found with the specified ID, no update performed.", content = @Content)
  })
  @PutMapping("/event/{eventId}")
  public ResponseEntity<Void> updateEventInDatabase(@PathVariable int eventId, @Valid @RequestBody Event event) {
    if (this.eventService.updateEvent(eventId, event)) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
  
  /**
   * Sets the public visibility of a event (only for admins)
   * 
   * @param eventId the id of the event
   * @param publiclyVisible the visibility of the event
   */
  @Operation(summary = "Changes public visibility of an event", description = "Sets the boolean value that determines if registered and unregistered users can see or enter a Event page.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Public visibility of event has been set.", content = @Content),
    @ApiResponse(responseCode = "404", description = "Event not found with the specified ID, no update performed.", content = @Content)
  })
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/event/{eventId}/publicVisible")
  public ResponseEntity<Void> setEventVisibility(@PathVariable long eventId, @Valid @RequestBody boolean publiclyVisible) {
    try {
      this.eventService.setEventPublicVisibility(eventId, publiclyVisible);
      return ResponseEntity.noContent().build();
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Checks if a specific event is publicly visible.
   * 
   * @param eventId the ID of the event to check visibility for
   * @return ResponseEntity containing a boolean indicating if the event is publicly visible,
   *         or not found if the event does not exist
   */
  @Operation(summary = "Check if event is publicly visible", description = "Checks whether a specific event is publicly visible.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Visibility status retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(type = "boolean", description = "True if event is publicly visible, false otherwise."))),
      @ApiResponse(responseCode = "404", description = "Event not found with the specified ID", content = @Content)
  })
  @GetMapping("/event/{eventId}/check-public-visibility")
  public ResponseEntity<Boolean> checkEventPublicVisibility(@PathVariable long eventId) {
      boolean isPublic = this.eventService.checkEventPublicVisibility(eventId);
      return ResponseEntity.ok(isPublic);
  }

  /**
   * Returns true or false if the user is a admin or not.
   * 
   * @return true or false value if the user is a admin or not.
   */
  private boolean isCurrentUserAdmin() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth != null && auth.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
  }

}
