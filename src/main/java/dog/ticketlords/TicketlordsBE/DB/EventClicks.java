package dog.ticketlords.TicketlordsBE.DB;

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
 * Entity representation of the database event_clicks table.
 * Tracks when an unregistered user clicks on an event.
 * The composite primary key (event_id, user_id) prevents
 * the same user from clicking the same event twice.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "event_clicks")
public class EventClicks {

  /** The composite primary key consisting of event ID and user ID. */
  @EmbeddedId
  private EventClicksId id;

  /** The event that was clicked. */
  @Setter
  @ManyToOne
  @MapsId("eventId")
  @JoinColumn(name = "event_id", referencedColumnName = "event_id")
  private Event event;

  /** The unregistered user who clicked the event. */
  @Setter
  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private UnregisteredUser user;

  /** The timestamp when the click occurred. */
  @Setter
  @Column(name = "clicked_at", insertable = false, updatable = false)
  private LocalDateTime clickedAt;

  /**
   * Constructs an EventClicks entry.
   *
   * @param event the {@link Event} that was clicked.
   * @param user  the {@link UnregisteredUser} who clicked the event.
   */
  public EventClicks(Event event, UnregisteredUser user) {
    this.event = event;
    this.user = user;
    this.id = new EventClicksId(event.getEventID(), user.getUId());
  }
}
