package dog.ticketlords.TicketlordsBE.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dog.ticketlords.TicketlordsBE.dbentity.UserInterest;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

  @Query("SELECT ui.interestScore FROM UserInterest ui WHERE ui.user.userId = :userId AND ui.category.categoryId = :categoryId")
  List<UserInterest> get(@Param("userId") long userId, @Param("categoryId") long categoryId);
}
