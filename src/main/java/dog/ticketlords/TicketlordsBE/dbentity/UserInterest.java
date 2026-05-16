package dog.ticketlords.TicketlordsBE.dbentity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_interest_id", insertable = false, nullable = false, updatable = false)
  @Schema(accessMode = Schema.AccessMode.READ_ONLY, hidden = true)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private long id;
  /** The registered user this interest belongs to. */
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private RegisteredUser user;

  /** The category name of the interest. */
  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  /** The interest score for this category. */
  @Setter
  @Column(name = "clicked_at", nullable = false, insertable = false)
  private LocalDateTime clickedAt;

  /**
   * Constructs a UserInterest object.
   *
   * @param user     the {@link RegisteredUser} this interest belongs to.
   * @param category the category object.
   */
  public UserInterest(RegisteredUser user, Category category) {
    this.user = user;
    this.category = category;
  }
}
