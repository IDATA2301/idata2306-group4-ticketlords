package dog.ticketlords.TicketlordsBE.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dog.ticketlords.TicketlordsBE.DTO.VendorRatingDTO;
import dog.ticketlords.TicketlordsBE.dbentity.Review;
import dog.ticketlords.TicketlordsBE.dbentity.ReviewId;
import dog.ticketlords.TicketlordsBE.repositories.ReviewRepository;

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

  /**
   * Gets all reviews by a user's id.
   *
   * @param userId the id of the user to find reviews from.
   * @return all reviews the user has made.
   */
  public List<Review> getAllReviewsByUserId(long userId) {
    return this.reviewRepo.findAllByUser_UserId(userId);
  }

  /**
   * Gets a single review.
   *
   * @param userId   the id of the user who'se review is to be returned.
   * @param vendorId the id of the vendor for who the user has made the review
   *                 for.
   *
   * @return the review.
   */
  public Optional<Review> getReview(long userId, long vendorId) {
    ReviewId id = new ReviewId(userId, vendorId);
    return this.reviewRepo.findById(id);
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
   * Returns a dto of the vendor's name, with said vendor's average rating.
   *
   * @param vendorName the full (non case sensitive) name of the vendor.
   * @return dto of vendor's name and its average rating.
   */
  public VendorRatingDTO getAverageRatingForVendor(String vendorName) {
    BigDecimal avg = reviewRepo.getAverageRatingForVendor(vendorName);
    if (avg == null) {
      avg = BigDecimal.ZERO;
    }
    BigDecimal rounded = avg.setScale(1, RoundingMode.HALF_UP);
    VendorRatingDTO dto = new VendorRatingDTO(vendorName, rounded);
    return dto;
  }

  /**
   * Finds all vendors which match the substring parameter, and maps their name,
   * to the average rating of the vendor.
   *
   * @param vendorNameSubstring a substring of the vendorName to search reviews
   *                            from.
   * @return a list of VendorRatingDTO which holds the vendor's name, and that
   *         vendor's average rating.
   */
  public List<VendorRatingDTO> getAverageRatingForAllVendorsByName(String vendorNameSubstring) {
    List<Review> reviews = this.reviewRepo.findAllByBookingSite_TicketVendorIgnoreCaseContaining(vendorNameSubstring);

    return reviews.stream()
        .filter(r -> r != null && r.getBookingSite() != null && r.getBookingSite().getTicketVendor() != null)
        .collect(Collectors.groupingBy(r -> r.getBookingSite().getTicketVendor(),
            Collectors.mapping(Review::getScore, Collectors.toList())))
        .entrySet().stream().map(e -> {
          List<BigDecimal> scores = e.getValue();
          BigDecimal avg = scores.isEmpty() ? BigDecimal.ZERO
              : scores.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                  .divide(BigDecimal.valueOf(scores.size()), 1, RoundingMode.HALF_UP);
          return new VendorRatingDTO(e.getKey(), avg);
        })
        .collect(Collectors.toList());
  }

}
