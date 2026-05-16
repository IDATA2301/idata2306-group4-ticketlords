package dog.ticketlords.TicketlordsBE.dbentity;

import java.math.BigDecimal;

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
 * Entity representation of the database ticket table.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "ticket")
public class Ticket {

  @Id
  @Column(name = "ticket_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(accessMode = Schema.AccessMode.READ_ONLY, hidden = true)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private long ticketId;

  // An event can have many tickets.
  @Setter
  @ManyToOne
  @JoinColumn(name = "event_id", referencedColumnName = "event_id")
  private Event event;
  @Setter
  @Column(name = "ticket_type")
  private String ticketType;
  @Setter
  @Column(name = "price", nullable = false)
  private BigDecimal price;
  @Setter
  @Column(name = "amount_available", nullable = false)
  private int amountAvailable;

  @Setter
  @Column(name = "ticket_description")
  private String ticketDescription;

  /**
   * Constructs a ticket object.
   * 
   * @param event             the event a ticket is for.
   * @param ticketType        The type of ticket; Normal, VIP etc...
   * @param price             The price of the ticket.
   * @param amountAvailable   The amount of tickets available.
   * @param ticketDescription A description of the ticket.
   */
  public Ticket(Event event, String ticketType, BigDecimal price, int amountAvailable,
      String ticketDescription) {
    this.event = event;
    this.ticketType = ticketType;
    this.price = price;
    this.amountAvailable = amountAvailable;
    this.ticketDescription = ticketDescription;
  }

}
