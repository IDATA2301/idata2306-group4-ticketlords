package dog.ticketlords.TicketlordsBE.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dog.ticketlords.TicketlordsBE.dbentity.Review;
import dog.ticketlords.TicketlordsBE.repositories.ReviewRepository;
import dog.ticketlords.TicketlordsBE.entity.VendorRating;

@Service
@Transactional
public class ReviewService {

  private final ReviewRepository reviewRepo;

  /**
   * Creates a new Service for Review operations.
   * 
   * @param revRepo the repository to perform persistence operations for
   *                {@link Review}
   */
  public ReviewService(ReviewRepository revRepo) {
    this.reviewRepo = revRepo;
  }

  public List<Review> getAllReviewsByUserId(long userId) {
    return this.reviewRepo.findAllByUser_UserId(userId);
  }

  /**
   * Inserts a review of a vendor into the database, as long as the user hasn't
   * made a review of that vendor already.
   * 
   * @param review The review to save into the database.
   *
   * @return true if successfully inserted, false otherwise.
   */
  public boolean insertReviewToDatabase(Review review) {
    if (!this.reviewRepo.existsByUser_UserIdAndBookingSiteTicketVendorIgnoreCaseContaining(review.getUser().getUserId(),
        review.getBookingSite().getTicketVendor())) {
      this.reviewRepo.save(review);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Returns the average rating for a specific vendor.
   *
   * @param vendorName the full (non case sensitive) name of the vendor.
   * @return the average rating of the vendor.
   */
  public double getAverageRatingForVendor(String vendorName) {
    return this.reviewRepo.findByBookingSite_TicketVendorIgnoreCase(vendorName);
  }

  // TODO: Need to create test for this method, to guarentee correctness.

  /**
   * Finds all vendors which match the substring parameter, and maps their name,
   * to the average rating of the vendor.
   *
   * @param vendorNameSubstring a substring of the vendorName to search reviews from.
   * @return a list of VendorRating which holds the vendor's name, and that
   *         vendor's average rating.
   */
  public List<VendorRating> getAverageRatingForAllVendorsByName(String vendorNameSubstring) {

    List<Review> reviews = this.reviewRepo.findAllByBookingSite_TicketVendorIgnoreCaseContaining(vendorNameSubstring);
    return reviews.stream()
            .filter(r -> r != null && r.getBookingSite() != null && r.getBookingSite().getTicketVendor() != null)
        .collect(Collectors.groupingBy(r -> r.getBookingSite().getTicketVendor(),
            Collectors.averagingDouble(Review::getScore)))
        .entrySet().stream().map(e -> new VendorRating(e.getKey(), e.getValue())).collect(Collectors.toList());
  }
}
