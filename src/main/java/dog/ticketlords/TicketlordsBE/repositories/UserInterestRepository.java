package dog.ticketlords.TicketlordsBE.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.dbentity.UserInterest;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

  List<UserInterest> findByUser_UserId(long userId);

}
