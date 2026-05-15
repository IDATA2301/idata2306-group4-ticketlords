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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Reviews", description = "!!! DEPRECATED !!!")
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

  @Operation(summary = "Add a review to the database", description = "Adds a review to the database. The review must contain a user ID, a ticket vendor ID, a rating, and an optional comment.", deprecated = true)
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
   * @param userId   the ID of the user
   * @param vendorId the ID of the ticket vendor
   * @return ResponseEntity containing the review, or not found if review does not
   *         exist
   */
  @Operation(summary = "Get a review by user ID and ticket vendor ID", description = "Retrieves a specific review by user ID and ticket vendor ID. Returns the review if found, or a 404 Not Found response if the review does not exist.", deprecated = true)
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
  @Operation(summary = "Get all reviews by user ID", description = "Retrieves all reviews written by a specific user. Returns a list of reviews, or an empty list if the user has not written any reviews.", deprecated = true)
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Review>> getAllReviewsByUserId(@PathVariable long userId) {
    return ResponseEntity.ok(this.reviewService.getAllReviewsByUserId(userId));
  }

  /**
   * Deletes a user's review on the specified ticket vendor.
   *
   * @param userId        the ID of the user whose review is to be deleted
   * @param bookingSiteId the ID of the ticket vendor whose review is to be
   *                      deleted
   * 
   * @return ResponseEntity with a 204 No Content status if the review was
   *         deleted, or a 404 Not Found status if the review was not found.
   */
  @Operation(summary = "Delete a user's review for a ticket vendor", description = "Deletes a user's review on the specified ticket vendor. Returns a 204 No Content status if the review was deleted successfully, or a 404 Not Found status if the review was not found in the database.", deprecated = true)
  @DeleteMapping("/user/{userId}/bookingSite/{bookingSiteId}")
  public ResponseEntity<Void> deleteReview(@PathVariable int userId, @PathVariable int bookingSiteId) {
    boolean removed = this.reviewService.deleteReview(userId, bookingSiteId);
    if (!removed) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }

  /**
   * Updates an existing review in the database.
   *
   * @param review the review object containing the updated information. The
   *               review
   *               must contain a valid user ID and ticket vendor ID to identify
   *               the review to be updated.
   *
   * @return ResponseEntity with a 204 No Content status if the review was
   *         updated successfully, or a 404 Not Found status if the review was not
   *         found in the database.
   */
  @PutMapping("/review/{reviewId}")
  @Operation(summary = "Update an existing review", description = "Updates an existing review in the database. The review must contain a valid user ID and ticket vendor ID to identify the review to be updated. Returns a 204 No Content status if the review was updated successfully, or a 404 Not Found status if the review was not found in the database.", deprecated = true)
  public ResponseEntity<Void> updateReviewInDatabase(@Valid @RequestBody Review review) {
    if (this.reviewService.updateReview(review)) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Get all reviews for a ticket vendor", description = "Retrieves all reviews for a specific ticket vendor. Returns a list of reviews, or an empty list if there are no reviews for the specified ticket vendor.", deprecated = true)
  @GetMapping("/bookingSite/{vendorId}")
  public ResponseEntity<List<Review>> getReviewsForBookingSite(long vendorId) {
    List<Review> reviews = this.reviewService.getReviewsForBookingSite(vendorId);
    if (reviews.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(reviews);
  }

}
