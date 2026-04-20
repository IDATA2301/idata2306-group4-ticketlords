package dog.ticketlords.TicketlordsBE.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dog.ticketlords.TicketlordsBE.dbentity.UserInterest;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

  List<UserInterest> findByUser_UserId(long userId);

  @Query("SELECT MAX(ui.clickedAt) FROM UserInterest ui WHERE ui.user.userId = :userId AND ui.category.categoryId = :categoryId")
  Optional<LocalDateTime> findMostRecentInterestByUserAndCategory(@Param("userId") long userId,
      @Param("categoryId") long categoryId);

}
