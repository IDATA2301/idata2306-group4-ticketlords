package dog.ticketlords.TicketlordsBE.DB;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the composite key for {@link Review}.
 * It only holds the two values that make up the composite key for review.
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewId implements Serializable {

  @Column(name = "user_id")
  private int userId;
  @Column(name = "ticket_vendor")
  private String ticketVendor;

  /**
   * Constructs a ReviewId. Should only be used for testing.
   *
   * @param userId       the user ID.
   * @param ticketVendor the ticket vendor name.
   */
  public ReviewId(int userId, String ticketVendor) {
    this.userId = userId;
    this.ticketVendor = ticketVendor;
  }

  /**
   * Checks wheter the object o is the same as this object.
   *
   * @param o the object to check this against.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof ReviewId))
      return false;
    ReviewId that = (ReviewId) o;
    return this.userId == that.userId && Objects.equals(this.ticketVendor, that.ticketVendor);
  }

  /**
   * Generates a hashCode of the class.
   */
  @Override
  public int hashCode() {
    return Objects.hash(userId, ticketVendor);
  }
}
