package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.entity.EventVenue;

public interface EventVenueRepository extends JpaRepository<EventVenue, Long> {
}
