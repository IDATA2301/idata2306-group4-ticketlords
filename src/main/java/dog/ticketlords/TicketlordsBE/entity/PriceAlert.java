package dog.ticketlords.TicketlordsBE.entity;

import java.math.BigDecimal;

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
 * Entity representation of the database price_alert table.
 * Represents a price alert set by a registered user for an event,
 * triggering when the ticket price reaches the target price.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "price_alert")
public class PriceAlert {

  /** The unique identifier of the price alert. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "alert_id")
  private int alertId;

  /** The registered user who set the alert. */
  @Setter
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private RegisteredUser user;

  /** The event the alert is for. */
  @Setter
  @ManyToOne
  @JoinColumn(name = "event_id", referencedColumnName = "event_id")
  private Event event;

  /** The target price the user wants to be alerted at. */
  @Setter
  @Column(name = "target_price")
  private BigDecimal targetPrice;

  /**
   * Whether the user will be notified or not if the event's price reaches user's
   * target.
   */
  @Setter
  @Column(name = "is_active")
  private boolean isActive = true;

  /**
   * Constructs a PriceAlert. Should only be used for testing.
   *
   * @param user        the {@link RegisteredUser} who set the alert.
   * @param event       the {@link Event} the alert is for.
   * @param targetPrice the target price for the alert.
   * @param isActive    whether the alert is active.
   */
  public PriceAlert(RegisteredUser user, Event event, BigDecimal targetPrice, boolean isActive) {
    this.user = user;
    this.event = event;
    this.targetPrice = targetPrice;
    this.isActive = isActive;
  }
}
