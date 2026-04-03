package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.entity.RegisteredUser;

public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {
}
