package dog.ticketlords.TicketlordsBE.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dog.ticketlords.TicketlordsBE.entity.Review;
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

}
