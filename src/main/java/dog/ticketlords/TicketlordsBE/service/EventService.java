package dog.ticketlords.TicketlordsBE.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import dog.ticketlords.TicketlordsBE.dbentity.Event;
import dog.ticketlords.TicketlordsBE.repositories.EventRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
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
   * @return an optional which either contains or doesn't contain the event.
   */
  public Optional<Event> getEvent(long eventId) {
    return this.eventRepo.findById(eventId);
  }

  /**
   * Inserts an event into the database, as long as it doesn't already exist.
   */
  public boolean insertEventIntoDatabase(Event event) {
    if (!this.eventRepo.existsById(event.getEventId())) {
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
   *         If no events are found, return an empty list.
   */
  public List<Event> getAllEvents() {
    return this.eventRepo.findAll();
  }

  /**
   * Finds the top 10 events based on the most clicks.
   *
   * @return A list containing the top 10 events
   */
  public List<Event> getPopularEvents() {
    Pageable pageable = PageRequest.of(0, 10);
    return this.eventRepo.findAllByOrderByTotalClicksDesc(pageable);
  }

  /**
   * Updates the field of a specified {@link Event} with a provided dummy Event
   * object. The dummy event does not need to hold all values defined in an Event,
   * as each value is checked against null before being set. Resulting in only
   * meaningful updating of the original Event.
   *
   * @param eventId      the id of the event to edit.
   * @param updatedEvent the dummy whose fields are to be copied over to the
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
   * Deletes a {@link Event} column in the database if it exists.
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
   * Searches the database for events, where the event itself, it's host, or its
   * category matches the param.
   *
   * @param searchTerm the string to search for an event by.
   * @return a {@link List} of all {@link Event} matching the search.
   */
  public List<Event> searchEvents(String searchTerm) {
    return this.eventRepo
        .findDistinctByEventNameContainingIgnoreCaseOrHostContainingIgnoreCaseOrCategory_CategoryNameContainingIgnoreCase(
            searchTerm, searchTerm, searchTerm);
  }

  /**
   * Retrieves the image name an eventId is created with.
   *
   * @param eventId the id of the event to find image from.
   * @return the image name an event is affiliated with.
   */
  public String getEventImageName(long eventId) throws IOException {

    String filename = eventRepo.findUrlById(eventId);
    if (filename == null || filename.isBlank()) {
      throw new IOException("Image not found for event with id: " + eventId);
    }
    return filename;

  }

  /**
   * Finds all events by a category's non-case sensitive name.
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

  /**
   * Retrieves all events that have already ended.
   *
   * @return A list of past events.
   */
  public List<Event> getPastEvents() {
    LocalDateTime now = LocalDateTime.now();
    return eventRepo.findByEventDateEndBefore(now);
  }

  /**
   * Searches for events with names containing the specified substring
   * (case-insensitive).
   *
   * @param namePart The substring to search for in event names.
   * @return A list of events whose names contain the given substring.
   */
  public List<Event> getEventsByName(String namePart) {
    return this.eventRepo.findByEventNameContainingIgnoreCase(namePart);
  }

  /**
   * Retrieves all events organized by the specified hostName string
   * (case-insensitive).
   *
   * @param hostName The name of the host.
   * @return A list of events organized by the given host.
   */
  public List<Event> getEventsByHostName(String hostName) {
    return eventRepo.findByHostIgnoreCase(hostName);
  }

  /**
   * Returns the total number of events.
   *
   * @return The count of all events.
   */
  public long countAllEvents() {
    return eventRepo.count();
  }

  /**
   * Returns the total number of events, based on a category's, or substring of
   * category's name.
   *
   * @param categoryNameSubstring the category's name to find amount of events
   *                              from.
   * @return the amount of events in the specified category.
   */
  public long countEventsByCategoryName(String categoryNameSubstring) {
    return this.eventRepo.countByCategory_CategoryNameContainingIgnoreCase(categoryNameSubstring);
  }

  /**
   * Retrieves a paginated list of events.
   *
   * @param page The page number (0-based).
   * @param size The number of events per page.
   * @return A list of events for the specified page.
   */
  public List<Event> getEventsPaged(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return eventRepo.findAll(pageable).getContent();
  }

}
