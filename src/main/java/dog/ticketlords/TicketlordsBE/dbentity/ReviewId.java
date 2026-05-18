package dog.ticketlords.TicketlordsBE.dbentity;

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
  private long userId;
  @Column(name = "ticket_vendor_id")
  private long ticketVendorId;

  /**
   * Constructs a ReviewId.
   * 
   * @param userId         the user ID.
   * @param ticketVendorId the ticket vendor's id.
   */
  public ReviewId(long userId, long ticketVendorId) {
    this.userId = userId;
    this.ticketVendorId = ticketVendorId;
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
    return this.userId == that.userId && Objects.equals(this.ticketVendorId, that.ticketVendorId);
  }

  /**
   * Generates a hashCode of the class.
   */
  @Override
  public int hashCode() {
    return Objects.hash(userId, ticketVendorId);
  }
}
