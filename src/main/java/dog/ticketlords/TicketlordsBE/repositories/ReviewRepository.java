package dog.ticketlords.TicketlordsBE.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.entity.Review;
import dog.ticketlords.TicketlordsBE.entity.ReviewId;

public interface ReviewRepository extends JpaRepository<Review, ReviewId> {
  List<Review> findAllByUser_UserId(long userId);

  boolean existsByUser_UserIdAndBookingSiteTicketVendorIgnoreCaseContaining(long userId, String vendorName);

  List<Review> findAllByBookingSite_TicketVendorIgnoreCaseContaining(String vendorNameSubstring);

  List<Review> findByBookSite_TicketVendorIgnoreCase(String vendorName);
}
