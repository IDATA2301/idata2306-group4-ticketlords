package dog.ticketlords.TicketlordsBE.DB;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
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
  private RegisteredUser registeredUser;

  @Column(name = "ticket_vendor")
  @MapsId("ticketVendor")
  private String ticketVendor;

  @Max(5)
  @Min(1)
  @Column(name = "score")
  private int score;

  /**
   * Constructs an instance of Review. Should be used for testing only.
   *
   * @param revId          the id which uniquely represents the specific review.
   * @param registeredUser the composite key which links a user to a review.
   * @param ticketVendor   the vendor the review should be for.
   * @param score          the score of the review.
   */
  public Review(ReviewId revId, RegisteredUser registeredUser, String ticketVendor, int score) {
    this.id = revId;
    this.registeredUser = registeredUser;
    this.score = score;
    this.ticketVendor = ticketVendor;
  }
}
