package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.entity.UserInterest;

public interface UserInterestRepository extends JpaRepository<UserInterest, Integer> {
}
