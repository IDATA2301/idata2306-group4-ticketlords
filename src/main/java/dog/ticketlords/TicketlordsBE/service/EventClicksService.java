package dog.ticketlords.TicketlordsBE.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dog.ticketlords.TicketlordsBE.dbentity.Event;
import dog.ticketlords.TicketlordsBE.dbentity.EventClicks;
import dog.ticketlords.TicketlordsBE.dbentity.EventClicksId;
import dog.ticketlords.TicketlordsBE.dbentity.UnregisteredUser;
import dog.ticketlords.TicketlordsBE.repositories.EventClicksRepository;

@Service
public class EventClicksService {

  private EventClicksRepository eventClicksRepository;

  public EventClicksService(EventClicksRepository eventClicksRepository) {
    this.eventClicksRepository = eventClicksRepository;
  }

  /**
   * Records a user's click on an event.
   */
  public void recordClick(Event event, UnregisteredUser user) {
    if (!hasUserClickedEvent(event, user)) {
      this.eventClicksRepository.save(new EventClicks(event, user));
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
   * Checks whether a user has already clicked on an event.
   * 
   * @param event    the event to check a click against.
   * @param uRegUser the unregisteredUser to check if has clicked an event.
   *
   * @return true if an EventClicksId with event and uRegUser already exists,
   *         false otherwise.
   */
  private boolean hasUserClickedEvent(Event event, UnregisteredUser uRegUser) {
    EventClicksId id = new EventClicksId(event.getEventId(), uRegUser.getUId());
    if (this.eventClicksRepository.existsById(id)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Finds all the recorded clicks for a specified event.
   *
   * @param eventId the event to find clicks from.
   * @return A list containing all EventClick objects. Empty list if no records in
   *         the database.
   */
  public List<EventClicks> getClicksForEvent(long eventId) {
    return this.eventClicksRepository.findAllByEvent_EventId(eventId);

  }

}
