package dog.ticketlords.TicketlordsBE.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dog.ticketlords.TicketlordsBE.dbentity.Review;
import dog.ticketlords.TicketlordsBE.dbentity.ReviewId;

public interface ReviewRepository extends JpaRepository<Review, ReviewId> {
  List<Review> findAllByUser_UserId(long userId);

  boolean existsByUser_UserIdAndBookingSiteTicketVendorIgnoreCaseContaining(long userId, String vendorName);

  List<Review> findAllByBookingSite_TicketVendorIgnoreCaseContaining(String vendorNameSubstring);

  @Query("SELECT AVG(r.score) FROM Review r WHERE LOWER(r.bookingSite.ticketVendor) = LOWER(:vendorName)")
  BigDecimal getAverageRatingForVendor(@Param("vendorName") String vendorName);
}
