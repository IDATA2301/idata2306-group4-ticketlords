package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
