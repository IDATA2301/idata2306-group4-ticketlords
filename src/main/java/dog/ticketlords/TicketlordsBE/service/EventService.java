package dog.ticketlords.TicketlordsBE.service;

import dog.ticketlords.TicketlordsBE.repositories.EventRepository;

public class EventService {

  private final EventRepository eventRepo;

  /**
   * Creates a new service for Event operations.
   * 
   * @param eventRepo the repository to perform persistence operations for {@link EventRepository}
   */
  public EventService(EventRepository eventRepo) {
    this.eventRepo = eventRepo;
  }

}
