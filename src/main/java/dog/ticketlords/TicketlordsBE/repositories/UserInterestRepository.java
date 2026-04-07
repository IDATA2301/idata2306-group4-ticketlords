package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.dbentity.UserInterest;
import dog.ticketlords.TicketlordsBE.dbentity.UserInterestId;

public interface UserInterestRepository extends JpaRepository<UserInterest, UserInterestId> {
}
