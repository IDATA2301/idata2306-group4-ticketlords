package dog.ticketlords.TicketlordsBE.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dog.ticketlords.TicketlordsBE.dbentity.Event;
import dog.ticketlords.TicketlordsBE.dbentity.EventClicks;
import dog.ticketlords.TicketlordsBE.dbentity.UnregisteredUser;
import dog.ticketlords.TicketlordsBE.service.EventClicksService;
import dog.ticketlords.TicketlordsBE.service.EventService;
import dog.ticketlords.TicketlordsBE.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Event Clicks", description = "APIs for managing event click tracking and retrieval")
@RestController
@RequestMapping("/api/event-clicks")
public class EventClicksController {

  private EventClicksService eventClicksService;
  private EventService eventService;
  private UserService userService;

  public EventClicksController(EventClicksService eventClicksService, EventService eventService,
      UserService userService) {
    this.eventClicksService = eventClicksService;
    this.eventService = eventService;
    this.userService = userService;
  }

  /**
   * Finds the amount of recorded clicks on a singular event.
   *
   * @param eventId the id of the event to find clicks for.
   * @return 200 OK with the click count inside.
   */
  @Operation(summary = "Get click count for an event", description = "Returns the total number of clicks recorded for a specific event.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Click count retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(type = "integer", format = "int64", description = "The total number of clicks recorded for the specified event.", example = "42"))),
      @ApiResponse(responseCode = "404", description = "Event not found", content = @Content(schema = @Schema(type = "null", description = "No event exists with the provided ID.")))
  })
  @GetMapping("/{eventId}")
  public ResponseEntity<Long> getEventClickCountById(@PathVariable long eventId) {
    long clickAmount = this.eventClicksService.getClickCountForEvent(eventId);
    return ResponseEntity.ok(clickAmount);
  }

  /**
   * Records a click on an event to the database, and increments the clickCount of
   * that event.
   *
   * @param eventId     the id of the event to register a click on.
   * @param unregUserId the id of the user that registers the click.
   *
   * @return ResponseEntity ok if successful, not found if params cant be found.
   */
  @Operation(summary = "Save event click", description = "Save an event click that happened between a user, and an event. This is used to track the user's most recent interests. If the click was tracked, update the event's click count")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Registered the click between the user and event, and increment the event's click count by one"),
      @ApiResponse(responseCode = "404", description = "Either the event or user could not be found with the provided IDs")
  })
  @PostMapping("/{eventId}/{userId}")
  public ResponseEntity<Void> saveEventClick(@PathVariable long eventId, @PathVariable long userId) {
    Optional<Event> clickedEvent = this.eventService.getEvent(eventId);
    Optional<UnregisteredUser> user = this.userService.getUnregUser(userId);
    if (clickedEvent.isPresent() && user.isPresent()) {
      boolean recorded = this.eventClicksService.recordClick(clickedEvent.get(), user.get());
      if (recorded) {
        this.eventService.incrementEventClickCount(eventId);
      }
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.notFound().build();
  }

  /**
   * Retrieves a list of all {@link EventClicks} that are recorded by a specific
   * event's id.
   *
   * @param eventId the id of the event to find entries by.
   * @return OK 200 with list of EventClicks as body if successful, 404 NOT FOUND
   *         if no event in the database by eventId param.
   */
  @Operation(summary = "Unused", deprecated = true)
  @GetMapping("/{eventId}/full")
  public ResponseEntity<List<EventClicks>> getAllEventClicksById(long eventId) {
    List<EventClicks> ecList = this.eventClicksService.getEventClicksForEvent(eventId);
    if (!ecList.isEmpty()) {
      return ResponseEntity.ok(ecList);
    }
    return ResponseEntity.notFound().build();
  }
}
