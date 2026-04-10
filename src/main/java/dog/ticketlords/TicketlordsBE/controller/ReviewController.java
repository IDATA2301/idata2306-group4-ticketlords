package dog.ticketlords.TicketlordsBE.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dog.ticketlords.TicketlordsBE.dbentity.Review;
import dog.ticketlords.TicketlordsBE.service.ReviewService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
  private final ReviewService reviewService;

  /**
 * Constructs a ReviewController with the provided ReviewService.
 * 
 * @param reviewService the review service to be used
 */
  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }


  @PostMapping("/review")
  public ResponseEntity<Void> addReviewToDatabase(@Valid @RequestBody Review review) {
    if (this.reviewService.insertReviewToDatabase(review)) {
      return ResponseEntity.created(URI.create("/reviews/review/" + review.getId())).build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }
  
  /**
   * Retrieves a specific review by user ID and ticket vendor ID.
   * 
   * @param userId the ID of the user
   * @param vendorId the ID of the ticket vendor
   * @return ResponseEntity containing the review, or not found if review does not exist
   */
  @GetMapping("/user/{userId}/vendor/{vendorId}")
  public ResponseEntity<Review> getReview(@PathVariable long userId, @PathVariable long vendorId) {
    Optional<Review> review = this.reviewService.getReview(userId, vendorId);
    if (review.isPresent()) {
      return ResponseEntity.ok(review.get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Retrieves all reviews written by a specific user.
   * 
   * @param userId the ID of the user
   * @return ResponseEntity containing a list of all reviews by the user
   */
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Review>> getAllReviewsByUserId(@PathVariable long userId) {
    return ResponseEntity.ok(this.reviewService.getAllReviewsByUserId(userId));
  }

  //TODO: add missing reviewService method: getAverageRatingForVendor

  //TODO: Remove comment when service is fully operational
  /*
  @DeleteMapping("/user/{userId}/bookingSite/{bookingSiteId}")
  public ResponseEntity<Void> removeReview(@PathVariable int userId, @PathVariable int bookingSiteId) {
    boolean removed = this.reviewService.removeReview(userId, bookingSiteId);
    if (!removed) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/review/{reviewId}")
  public ResponseEntity<Void> updateReviewInDatabase(@PathVariable int reviewId, @Valid @ResponseBody Review review) {
    if (this.reviewService.updateReview(reviewId, review)) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
  
  @GetMapping("/")
  public ResponseEntity<List<Review>> getAllReviews() {
    return ResponseEntity.ok(this.reviewService.getAllReviews());
  }
  */

}
