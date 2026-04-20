package dog.ticketlords.TicketlordsBE.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
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
  @RequestMapping("/{eventId}")
  public ResponseEntity<Long> getEventClickCountById(@PathVariable long eventId) {
    long clickAmount = this.eventClicksService.getClickCountForEvent(eventId);
    return ResponseEntity.ok(clickAmount);
  }

  /**
   * Records a click on an event to the database.
   *
   * @param eventId     the id of the event to register a click on.
   * @param unregUserId the id of the user that registers the click.
   *
   * @return ResponseEntity ok if successful, not found if params cant be found.
   */
  @PostMapping("/{eventId}/{unregUser}")
  public ResponseEntity<Void> saveEventClick(@PathVariable long eventId, @PathVariable long unregUserId) {
    Optional<Event> clickedEvent = this.eventService.getEvent(eventId);
    Optional<UnregisteredUser> user = this.userService.getUnregUser(unregUserId);
    if (clickedEvent.isPresent() && user.isPresent()) {
      this.eventClicksService.recordClick(clickedEvent.get(), user.get());
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
  @RequestMapping("/{eventId}/full")
  public ResponseEntity<List<EventClicks>> getAllEventClicksById(long eventId) {
    List<EventClicks> ecList = this.eventClicksService.getEventClicksForEvent(eventId);
    if (!ecList.isEmpty()) {
      return ResponseEntity.ok(ecList);
    }
    return ResponseEntity.notFound().build();
  }
}
