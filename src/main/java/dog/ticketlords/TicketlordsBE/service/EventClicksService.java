package dog.ticketlords.TicketlordsBE.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import dog.ticketlords.TicketlordsBE.dbentity.Event;
import dog.ticketlords.TicketlordsBE.dbentity.EventClicks;
import dog.ticketlords.TicketlordsBE.dbentity.EventClicksId;
import dog.ticketlords.TicketlordsBE.dbentity.UnregisteredUser;
import dog.ticketlords.TicketlordsBE.DTO.CategoryClicksDTO;
import dog.ticketlords.TicketlordsBE.repositories.EventClicksRepository;

@Service
public class EventClicksService {

  private EventClicksRepository eventClicksRepository;

  public EventClicksService(EventClicksRepository eventClicksRepository) {
    this.eventClicksRepository = eventClicksRepository;
  }

  /**
   * Records a user's click on an event if it doesnt exist already. If a previous
   * click does exist however,
   * simply update the last interacted at field, with now.
   *
   * @param event The {@link Event} that was clicked.
   * @param user  the {@link UnregisteredUser} that clicked the event.
   */
  public boolean recordClick(Event event, UnregisteredUser user) {
    if (!this.eventClicksRepository.existsById(new EventClicksId(event.getEventId(), user.getUId()))) {
      this.eventClicksRepository.save(new EventClicks(event, user));
      return true;
    }

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime lastClickTime = this.eventClicksRepository.findLastInteraction(event.getEventId(), user.getUId());
    boolean hasPassedOneMinuteOrMore = ChronoUnit.MINUTES.between(lastClickTime, now) >= 1;

    if (hasPassedOneMinuteOrMore) {
      EventClicks ec = this.eventClicksRepository
          .getReferenceById(new EventClicksId(event.getEventId(), user.getUId()));
      ec.setLastInteraction(now);
      this.eventClicksRepository.save(ec);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Finds all event click entries in the database, for the specified event.
   *
   * @return the amount of individual user's who have clicked on the specified
   *         event.
   */
  public long getClickCountForEvent(long eventId) {
    return this.eventClicksRepository.countByEvent_EventId(eventId);
  }

  /**
   * Finds all the recorded clicks for a specified event.
   *
   * @param eventId the event to find clicks from.
   * @return A list containing all EventClick objects. Empty list if no records in
   *         the database.
   */
  public List<EventClicks> getEventClicksForEvent(long eventId) {
    if (this.eventClicksRepository.existsByEvent_EventId(eventId)) {
      return this.eventClicksRepository.findAllByEvent_EventId(eventId);
    } else {
      return List.of();
    }
  }

  // TODO: Think we should remove this. Use UserInterest's method instead.
  /**
   * Retrieves aggregated click data for a user per category. Maps user interests
   * based on the number of events the user has clicked in each category.
   *
   * @param userId the user ID to retrieve category interests for.
   * @return A list of CategoryClicksDTO objects ordered by click count
   *         descending. Empty list if user has no clicks.
   */
  public List<CategoryClicksDTO> getUserInterestByCategory(long userId) {
    return this.eventClicksRepository.findClicksByUserPerCategory(userId);
  }

}
