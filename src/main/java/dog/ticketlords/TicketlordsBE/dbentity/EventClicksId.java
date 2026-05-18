package dog.ticketlords.TicketlordsBE.dbentity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the composite key for {@link EventClicks}.
 * It holds the two values that make up the composite key: event ID and user ID.
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventClicksId implements Serializable {

  @Column(name = "event_id")
  private long eventId;
  @Column(name = "user_id")
  private long userId;

  /**
   * Constructs an instance of EventClicksId.
   * 
   * @param eventId the id of a specific event.
   * @param userId  the id of the a specific user.
   */
  public EventClicksId(long eventId, long userId) {
    this.eventId = eventId;
    this.userId = userId;
  }

  /**
   * Checks whether the object o is the same as this object.
   *
   * @param o the object to check this against.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof EventClicksId))
      return false;
    EventClicksId that = (EventClicksId) o;
    return this.eventId == that.eventId && this.userId == that.userId;
  }

  /**
   * Generates a hashCode of the class.
   */
  @Override
  public int hashCode() {
    return Objects.hash(eventId, userId);
  }
}
