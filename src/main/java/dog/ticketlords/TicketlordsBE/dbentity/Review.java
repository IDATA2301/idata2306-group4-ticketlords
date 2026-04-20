package dog.ticketlords.TicketlordsBE.dbentity;

import java.math.BigDecimal;

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
import lombok.Setter;

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
  @JoinColumn(name = "ticket_vendor_id", referencedColumnName = "ticket_vendor_id")
  @MapsId("ticketVendorId")
  private BookingSite bookingSite;

  @Max(5)
  @Min(1)
  @Column(name = "score", precision = 2, scale = 1)
  @Setter
  private BigDecimal score;

  @Column(name = "review_content")
  @Setter
  private String reviewContent;

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if (that == null || getClass() != that.getClass()) {
      return false;
    }
    if (that instanceof Review) {
      Review thatReview = (Review) that;
      return this.id != null && this.getId().equals(thatReview.getId());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.id != null ? this.id.hashCode() : 0;
  }

  /**
   * Constructs an instance of Review.
   *
   * @param revId          the id which uniquely represents the specific review.
   * @param registeredUser the composite key which links a user to a review.
   * @param bookingSiteId  the id of vendor the review should be for.
   * @param score          the score of the review.
   * @param reviewContent  the user's review.
   */
  public Review(ReviewId revId, RegisteredUser registeredUser, BookingSite bookingSite, BigDecimal score,
      String reviewContent) {
    this.id = revId;
    this.user = registeredUser;
    this.score = score;
    this.bookingSite = bookingSite;
    this.reviewContent = reviewContent;
  }

  public static Builder builder() {
    return new Builder();
  }

  private Review(Builder b) {
    this.id = b.id;
    this.user = b.user;
    this.bookingSite = b.bookingSite;
    this.score = b.score;
    this.reviewContent = b.reviewContent;
  }

  public static class Builder {
    private ReviewId id;
    private RegisteredUser user;
    private BookingSite bookingSite;
    private BigDecimal score;
    private String reviewContent;

    public Builder id(ReviewId id) {
      this.id = id;
      return this;
    }

    public Builder user(RegisteredUser user) {
      this.user = user;
      return this;
    }

    public Builder bookingSite(BookingSite bookingSite) {
      this.bookingSite = bookingSite;
      return this;
    }

    public Builder score(BigDecimal score) {
      this.score = score;
      return this;
    }

    public Builder reviewContent(String reviewContent) {
      this.reviewContent = reviewContent;
      return this;
    }

    public Review build() {
      return new Review(this);
    }
  }
}
