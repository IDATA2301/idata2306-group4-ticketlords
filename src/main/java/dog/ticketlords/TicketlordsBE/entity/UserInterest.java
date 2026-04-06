package dog.ticketlords.TicketlordsBE.entity;

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
import lombok.Setter;

/**
 * Entity representation of the database user_interest table.
 * Represents a user's interest in a specific category, with a score
 * indicating the level of interest.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "user_interest")
public class UserInterest {

  /** The composite primary key (user_id, category_name). */
  @EmbeddedId
  private UserInterestId id;

  /** The registered user this interest belongs to. */
  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private RegisteredUser user;

  /** The category name of the interest. */
  @MapsId("categoryName")
  @ManyToOne
  @JoinColumn(name = "category_name")
  private Category category;

  /** The interest score for this category. */
  @Setter
  @Column(name = "interest_score", nullable = false)
  private int interestScore;

  /**
   * Constructs a UserInterest object.
   *
   * @param user          the {@link RegisteredUser} this interest belongs to.
   * @param category      the category object.
   * @param interestScore the interest score.
   */
  public UserInterest(RegisteredUser user, Category category, int interestScore) {
    this.user = user;
    this.category = category;
    this.interestScore = interestScore;
    this.id = new UserInterestId(user.getUnregisteredUser().getUId(), category.getCategoryName());
  }
}
