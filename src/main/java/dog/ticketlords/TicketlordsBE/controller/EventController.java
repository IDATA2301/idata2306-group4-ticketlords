package dog.ticketlords.TicketlordsBE.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dog.ticketlords.TicketlordsBE.dbentity.Event;
import dog.ticketlords.TicketlordsBE.service.EventService;

@RestController
@RequestMapping("/api/events")
public class EventController {

  private final EventService eventService;

  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @GetMapping("/event/{eventId}")
  public ResponseEntity<Event> getEvent(@PathVariable int eventId) {
    if (this.eventService.getEvent(eventId).isPresent()) {
      return ResponseEntity.ok(eventService.getEvent(eventId).get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/event/{eventId}") //TODO: fix
  public ResponseEntity<Void> insertEventIntoDatabase(@PathVariable int eventId) {
    boolean inserted = eventService.insertEventIntoDatabase(eventId);
  }

  @DeleteMapping("/event/{eventId}")
  public ResponseEntity<Void> removeEvent(@PathVariable int eventId) {
    boolean removed = this.eventService.deleteEvent(eventId);
    if (!removed) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
