package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.entity.UserInterest;
import dog.ticketlords.TicketlordsBE.entity.UserInterestId;

public interface UserInterestRepository extends JpaRepository<UserInterest, UserInterestId> {
}
