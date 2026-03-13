package dog.ticketlords.TicketlordsBE.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Composite primary key class for the {@link Wishlist} entity.
 * Consists of a user ID and an event ID representing the unique combination of
 * a user and an event in the wishlist table.
 *
 * <p>
 * This class is marked as {@link Embeddable} and implements
 * {@link Serializable}, as required by JPA
 * for composite keys
 * </p>
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishlistId implements Serializable {

  private int userId;
  private int eventId;

  /**
   * Creates a new WishlistId with the given user and event IDs.
   * Constructor is meant to be used only for testing purposes.
   *
   * @param userId  the ID of the user.
   * @param eventId the ID of the event.
   */
  public WishlistId(int userId, int eventId) {
    this.userId = userId;
    this.eventId = eventId;

  }

  /**
   * Checks equality based on userId and eventId
   *
   * @param o the object to compare against
   * @return true if both userId and eventId are equal.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof WishlistId))
      return false;
    WishlistId that = (WishlistId) o;
    return this.userId == that.userId && this.eventId == that.eventId;
  }

  /**
   * Generated a hash code based on userId and eventId.
   *
   * @return the hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(userId, eventId);
  }
}
