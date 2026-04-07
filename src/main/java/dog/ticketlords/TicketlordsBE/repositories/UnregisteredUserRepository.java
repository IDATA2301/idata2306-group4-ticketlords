package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.dbentity.UnregisteredUser;

public interface UnregisteredUserRepository extends JpaRepository<UnregisteredUser, Long> {
}
