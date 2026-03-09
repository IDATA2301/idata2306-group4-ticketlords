package dog.ticketlords.TicketlordsBE.DB;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "wishlist")
@Getter
public class Wishlist {

  @EmbeddedId
  private WishlistId id;

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  @Setter
  private RegisteredUser user;

  @ManyToOne
  @MapsId("eventId")
  @JoinColumn(name = "event_id", referencedColumnName = "event_id")
  @Setter
  private Event event;

  @Column(name = "added_at", insertable = false, updatable = false)
  private LocalDateTime addedAt;

}
