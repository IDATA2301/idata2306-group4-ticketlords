package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.dbentity.EventClicks;
import dog.ticketlords.TicketlordsBE.dbentity.EventClicksId;

public interface EventClicksRepository extends JpaRepository<EventClicks, EventClicksId> {
  long countByEvent_EventId(long eventId);
}
