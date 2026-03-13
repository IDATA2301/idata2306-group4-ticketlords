package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.entity.EventClicks;
import dog.ticketlords.TicketlordsBE.entity.EventClicksId;

public interface EventClicksRepository extends JpaRepository<EventClicks, EventClicksId> {
}
