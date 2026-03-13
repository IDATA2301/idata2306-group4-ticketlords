package dog.ticketlords.TicketlordsBE.entity;

import java.time.LocalDateTime;

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
 * Class representation of the wishlist database table.
 */
@Entity
@Table(name = "wishlist")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wishlist {

  // The composite key which represents this entity's "primary key"
  @EmbeddedId
  private WishlistId id;

  // One user can have multiple wishlists.
  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  @Setter
  private RegisteredUser user;

  // One event can have multiple wishlistings.
  @ManyToOne
  @MapsId("eventId")
  @JoinColumn(name = "event_id", referencedColumnName = "event_id")
  @Setter
  private Event event;

  // Automatically set at the database.
  @Column(name = "added_at", insertable = false, updatable = false)
  private LocalDateTime addedAt;

  /**
   * Creates a Wishlist object. Should only be used for testing.
   *
   * @param user  The user who has a wishlist.
   * @param event The event to wishlist.
   *
   */
  public Wishlist(RegisteredUser user, Event event) {
    this.id = new WishlistId(user.getUnregisteredUser().getUId(),
        event.getEventID());
    this.user = user;
    this.event = event;
  }
}
