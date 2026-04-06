package dog.ticketlords.TicketlordsBE.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * An entity representation of the review table in the database.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "review")
@Entity
public class Review {

  @EmbeddedId
  private ReviewId id;

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private RegisteredUser user;

  @ManyToOne
  @JoinColumn(name = "ticket_vendor", referencedColumnName = "ticket_vendor")
  @MapsId("ticketVendor")
  private BookingSite bookingSite;

  @Max(5)
  @Min(1)
  @Column(name = "score")
  private int score;

  @Column(name = "review_content")
  private String reviewContent;

  /**
   * Constructs an instance of Review.
   *
   * @param revId          the id which uniquely represents the specific review.
   * @param registeredUser the composite key which links a user to a review.
   * @param bookingSite    the vendor the review should be for.
   * @param score          the score of the review.
   * @param reviewContent  the user's review.
   */
  public Review(ReviewId revId, RegisteredUser registeredUser, BookingSite bookingSite, int score,
      String reviewContent) {
    this.id = revId;
    this.user = registeredUser;
    this.score = score;
    this.bookingSite = bookingSite;
    this.reviewContent = reviewContent;
  }
}
