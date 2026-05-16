package dog.ticketlords.TicketlordsBE.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.dbentity.RegisteredUser;

public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {

  Optional<RegisteredUser> findByEmail(String email);

  boolean existsByEmailIgnoreCase(String email);

}
