package dog.ticketlords.TicketlordsBE.service;

import java.util.List;
import java.util.Optional;

import dog.ticketlords.TicketlordsBE.entity.Event;
import dog.ticketlords.TicketlordsBE.repositories.EventRepository;
import jakarta.persistence.EntityNotFoundException;

public class EventService {

  private final EventRepository eventRepo;

  /**
   * Creates a new service for Event operations.
   * 
   * @param eventRepo the repository to perform persistence operations for
   *                  {@link EventRepository}
   */
  public EventService(EventRepository eventRepo) {
    this.eventRepo = eventRepo;
  }

  /**
   * Checks the database for an event.
   *
   * @param eventId the id of the event to look for.
   * @return an optional which either contains or doesnt contain the event.
   */
  public Optional<Event> getEvent(long eventId) {
    return this.eventRepo.findById(eventId);
  }

  /**
   * Inserts an event into the database, as long as it doesnt already exist.
   */
  public boolean insertEventIntoDatabase(Event event) {
    if (!this.eventRepo.existsById(event.getEventID())) {
      this.eventRepo.save(event);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Finds all events in the database.
   *
   * @return A list containing the event objects, found in the database.
   *         If no events are found, returns an empty list.
   */
  public List<Event> getAllEvents() {
    return this.eventRepo.findAll();
  }

  public boolean updateEvent(long eventId, Event updatedEvent) {
    try {
      Event existingEvent = this.eventRepo.findById(eventId)
          .orElseThrow(() -> new EntityNotFoundException("Event not found"));
      if (updatedEvent.getEventName() != null)
        existingEvent.setEventName(updatedEvent.getEventName());
      if (updatedEvent.getHost() != null)
        existingEvent.setHost(updatedEvent.getHost());
      if (updatedEvent.getCategory() != null)
        existingEvent.setCategory(updatedEvent.getCategory());
      if (updatedEvent.getEventDateStart() != null)
        existingEvent.setEventDateStart(updatedEvent.getEventDateStart());
      if (updatedEvent.getEventDateEnd() != null)
        existingEvent.setEventDateEnd(updatedEvent.getEventDateEnd());
      if (updatedEvent.getEventVenue() != null)
        existingEvent.setEventVenue(updatedEvent.getEventVenue());
      if (updatedEvent.getEventDescription() != null)
        existingEvent.setEventDescription(updatedEvent.getEventDescription());
      this.eventRepo.save(existingEvent);
      return true;
    } catch (EntityNotFoundException e) {
      return false;
    }
  }

  public boolean deleteEvent(long eventId) {
    if (this.eventRepo.existsById(eventId)) {
      this.eventRepo.deleteById(eventId);
      return true;
    }
    return false;
  }

  /**
   * Finds all events by a category's id.
   *
   * @return A list of all Events
   */
  public List<Event> getEventsByCategoryId(long id) {
    return this.eventRepo.findByCategory_CategoryId(id);
  }

  /**
   * Finds all events by a category's non case sensitive name.
   *
   * @return A list of all Events matching the query.
   */
  public List<Event> getEventsByCategoryName(String categoryName) {
    return this.eventRepo.findByCategory_CategoryNameIgnoreCase(categoryName);
  }
}
