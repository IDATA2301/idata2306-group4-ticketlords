package dog.ticketlords.TicketlordsBE.service;

import java.time.LocalDateTime;
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

  /**
   * Updates the field of a specified {@link Event} with a provided dummy Event
   * object. The dummy event does not need to hold all values defined in an Event,
   * as each value is checked against null before being set. Resulting in only
   * meaningful updating of the original Event.
   *
   * @param eventId      the id of the event to edit.
   * @param updatedEvent the dummy who'se fields are to be copied over to the
   *                     actual event in the database.
   */
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

  /**
   * Deletes a {@link Event} collumn in the database if it exists.
   *
   * @param eventId the id of the event to delete.
   *
   * @return true if the deletion succeeded, false otherwise.
   */
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
   * @param categoryName the name of the category to find events from.
   *
   * @return A list of all Events matching the query.
   */
  public List<Event> getEventsByCategoryName(String categoryName) {
    return this.eventRepo.findByCategory_CategoryNameIgnoreCase(categoryName);
  }

  /**
   * Finds all events by the specified date range.
   *
   * @param searchStart the date to start the search from.
   * @param searchEnd   the date to end the search upon reaching.
   *
   * @return A list of all events who'se date is set between the date range.
   */
  public List<Event> getEventsBetweenDates(LocalDateTime searchStart, LocalDateTime searchEnd) {
    return this.eventRepo.findByEventDateStartBetween(searchStart, searchEnd);
  }

  /**
   * Finds all events between the current time, and the specified date.
   *
   * @return A list of all {@link Event} matching the range.
   */
  public List<Event> getUpcomingEvents(LocalDateTime searchRangeEnd) {
    return this.eventRepo.findByEventDateStartAfter(searchRangeEnd);
  }

  /**
   * Finds all events that have yet to start.
   *
   * @return A list of all {@link Event}'s who'se start date is after the current
   *         time.
   */
  public List<Event> getAllUpcomingEvents() {
    return this.eventRepo.findByEventDateStartAfter(LocalDateTime.now());
  }

}
