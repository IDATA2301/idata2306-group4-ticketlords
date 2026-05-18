package dog.ticketlords.TicketlordsBE.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import dog.ticketlords.TicketlordsBE.DTO.EventRequestDTO;
import dog.ticketlords.TicketlordsBE.dbentity.Category;
import dog.ticketlords.TicketlordsBE.dbentity.Event;
import dog.ticketlords.TicketlordsBE.dbentity.EventVenue;
import dog.ticketlords.TicketlordsBE.exception.ResourceNotFoundException;
import dog.ticketlords.TicketlordsBE.dbentity.EventVenue;
import dog.ticketlords.TicketlordsBE.repositories.CategoryRepository;
import dog.ticketlords.TicketlordsBE.repositories.EventRepository;
import dog.ticketlords.TicketlordsBE.repositories.EventVenueRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class EventService {

  private final EventRepository eventRepo;
  private final CategoryRepository categoryRepo;
  private final EventVenueRepository eventVenueRepo;
  private final CategoryService categoryService;
  private final EventVenueService eventVenueService;

  /**
   * Creates a new service for Event operations.
   * 
   * @param eventRepo the repository to perform persistence operations for
   *                  {@link EventRepository}
   */
  public EventService(EventRepository eventRepo,
                      CategoryService categoryService,
                      EventVenueService eventVenueService,
                      CategoryRepository categoryRepo,
                      EventVenueRepository eventVenueRepo) {
    this.eventRepo = eventRepo;
    this.categoryRepo = categoryRepo;
    this.eventVenueRepo = eventVenueRepo;
    this.categoryService = categoryService;
    this.eventVenueService = eventVenueService;
  }

  /**
   * Checks if the current user is an admin.
   *
   * @return true if the current user has ROLE_ADMIN, false otherwise
   */
  private boolean isCurrentUserAdmin() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth != null && auth.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
  }

  /**
   * Filters events to only include publicly visible ones unless user is admin.
   *
   * @param events the list of events to filter
   * @return filtered list containing all events if user is admin, only public events otherwise
   */
  private List<Event> filterByVisibility(List<Event> events) {
    if (isCurrentUserAdmin()) {
      return events;
    }
    return events.stream()
        .filter(Event::isPubliclyVisible)
        .toList();
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
   * Inserts an event into the database.
   *
   * @param eventDTO the event to insert
   * @return the saved Event wrapped in an Optional, or empty if saving failed
   */
  public Optional<Event> insertEventIntoDatabase(EventRequestDTO eventDTO) {

    Optional<Category> category = this.categoryService.getCategoryByCategoryId(eventDTO.categoryId());
    Optional<EventVenue> eventVenue = this.eventVenueService.getEventVenueById(eventDTO.venueId());

    if (category.isEmpty() || eventVenue.isEmpty()) {
      throw new ResourceNotFoundException("Category or Venues not found.");
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
      eventDTO.imgPathUrl());
      Event saved = this.eventRepo.save(event);
      return Optional.of(saved);

  }

  /**
   * Finds all events in the database.
   *
   * @return A list containing the event objects, found in the database.
   *         If no events are found, return an empty list.
   *         Non-admin users only see publicly visible events.
   */
  public List<Event> getAllEvents() {
    return filterByVisibility(this.eventRepo.findAll());
  }

  /**
   * Finds the top 10 events based on the most clicks.
   *
   * @return A list containing the top 10 events
   *         Non-admin users only see publicly visible events.
   */
  public List<Event> getPopularEvents() {
    Pageable pageable = PageRequest.of(0, 10);
    return filterByVisibility(this.eventRepo.findAllByOrderByTotalClicksDesc(pageable));
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
  public void updateEvent(long eventId, EventRequestDTO updatedEvent)
      throws IllegalArgumentException, EntityNotFoundException {
    Event existingEvent = this.eventRepo.findById(eventId)
        .orElseThrow(() -> new EntityNotFoundException("Event not found"));

    if (updatedEvent.eventName() == null || updatedEvent.eventName().isBlank())
      throw new IllegalArgumentException("Event name is required.");
    if (updatedEvent.host() == null || updatedEvent.host().isBlank())
      throw new IllegalArgumentException("Host is required.");
    if (updatedEvent.categoryId() == null)
      throw new IllegalArgumentException("Category ID is required.");
    if (updatedEvent.eventDateStart() == null)
      throw new IllegalArgumentException("Event start date is required.");
    if (updatedEvent.eventDateEnd() == null)
      throw new IllegalArgumentException("Event end date is required.");
    if (updatedEvent.venueId() == null)
      throw new IllegalArgumentException("Event venue is required.");

    existingEvent.setEventName(updatedEvent.eventName());
    existingEvent.setHost(updatedEvent.host());

    Category category = this.categoryRepo.findById(updatedEvent.categoryId())
        .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    existingEvent.setCategory(category);

    existingEvent.setEventDateStart(updatedEvent.eventDateStart());
    existingEvent.setEventDateEnd(updatedEvent.eventDateEnd());

    EventVenue venue = this.eventVenueRepo.findById(updatedEvent.venueId())
        .orElseThrow(() -> new EntityNotFoundException("Event venue not found"));
    existingEvent.setEventVenue(venue);

    if (updatedEvent.eventDescription() != null) {
      existingEvent.setEventDescription(updatedEvent.eventDescription());
    }
    if (updatedEvent.imgPathUrl() != null) {
      existingEvent.setImgPathUrl(updatedEvent.imgPathUrl());
    }
    this.eventRepo.save(existingEvent);
  }

  /**
   * ncrements the clickCount of an event.
   * 
   * @param eventId the id of the event to increment clickcount of.
   */
  public void incrementEventClickCount(long eventId) {
    this.eventRepo.incrementClickCount(eventId);
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
   *         Non-admin users only see publicly visible events.
   */
  public List<Event> getEventsByCategoryId(long id) {
    return filterByVisibility(this.eventRepo.findByCategory_CategoryId(id));
  }

  /**
   * Searches the database for events, where the event itself, it's host, or its
   * category matches the param.
   *
   * @param searchTerm the string to search for an event by.
   * @return a {@link List} of all {@link Event} matching the search.
   *         Non-admin users only see publicly visible events.
   */
  public List<Event> searchEvents(String searchTerm) {
    return filterByVisibility(this.eventRepo.findDistinctByEventNameContainingIgnoreCaseOrHostContainingIgnoreCaseOrCategory_CategoryNameContainingIgnoreCaseOrEventVenue_CityContainingIgnoreCaseOrEventVenue_CountryContainingIgnoreCase(
            searchTerm, searchTerm, searchTerm, searchTerm, searchTerm));
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
   *         Non-admin users only see publicly visible events.
   */
  public List<Event> getEventsByCategoryName(String categoryName) {
    return filterByVisibility(this.eventRepo.findByCategory_CategoryNameIgnoreCase(categoryName));
  }

  /**
   * Finds all events by the specified date range.
   *
   * @param searchStart the date to start the search from.
   * @param searchEnd   the date to end the search upon reaching.
   *
   * @return A list of all events who'se date is set between the date range.
   *         Non-admin users only see publicly visible events.
   */
  public List<Event> getEventsBetweenDates(LocalDateTime searchStart, LocalDateTime searchEnd) {
    return filterByVisibility(this.eventRepo.findByEventDateStartBetween(searchStart, searchEnd));
  }

  /**
   * Finds all events between the current time, and the specified date.
   *
   * @return A list of all {@link Event} matching the range.
   *         Non-admin users only see publicly visible events.
   */
  public List<Event> getUpcomingEvents(LocalDateTime searchRangeEnd) {
    return filterByVisibility(this.eventRepo.findByEventDateStartAfter(searchRangeEnd));
  }

  /**
   * Finds all events that have yet to start.
   *
   * @return A list of all {@link Event}'s who'se start date is after the current
   *         time.
   *         Non-admin users only see publicly visible events.
   */
  public List<Event> getAllUpcomingEvents() {
    return filterByVisibility(this.eventRepo.findByEventDateStartAfter(LocalDateTime.now()));
  }

  /**
   * Retrieves all events that have already ended.
   *
   * @return A list of past events.
   *         Non-admin users only see publicly visible events.
   */
  public List<Event> getPastEvents() {
    LocalDateTime now = LocalDateTime.now();
    return filterByVisibility(eventRepo.findByEventDateEndBefore(now));
  }

  /**
   * Searches for events with names containing the specified substring
   * (case-insensitive).
   *
   * @param namePart The substring to search for in event names.
   * @return A list of events whose names contain the given substring.
   *         Non-admin users only see publicly visible events.
   */
  public List<Event> getEventsByName(String namePart) {
    return filterByVisibility(this.eventRepo.findByEventNameContainingIgnoreCase(namePart));
  }

  /**
   * Retrieves all events organized by the specified hostName string
   * (case-insensitive).
   *
   * @param hostName The name of the host.
   * @return A list of events organized by the given host.
   *         Non-admin users only see publicly visible events.
   */
  public List<Event> getEventsByHostName(String hostName) {
    return filterByVisibility(eventRepo.findByHostIgnoreCase(hostName));
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
   *         Non-admin users only see publicly visible events.
   */
  public List<Event> getEventsPaged(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return filterByVisibility(eventRepo.findAll(pageable).getContent());
  }

  /**
   * Sets an event's publiclyVisible boolean value.
   * 
   * @param eventId the id of the event.
   * @param publiclyVisible the visibility boolean value of the event.
   * @throws ResourceNotFoundException if no event with given ID exists.
   */
  public void setEventPublicVisibility(long eventId, boolean publiclyVisible) {
    if (!this.eventRepo.existsById(eventId)) {
      throw new ResourceNotFoundException("Event with ID " + eventId + " not found");
    }
    this.eventRepo.setPubliclyVisible(eventId, publiclyVisible);
  }

  /**
   * Checks a event's public visibility.
   * 
   * @return the event's public visibility boolean value.
   * @throws ResourceNotFoundException if no event with given ID exists.
   */
  public boolean checkEventPublicVisibility(long eventId) {
    if (!this.eventRepo.existsById(eventId)) {
      throw new ResourceNotFoundException("Event with ID " + eventId + " not found");
    }
    return this.eventRepo.existsByEventIdAndPubliclyVisibleIsTrue(eventId);
  }

}
