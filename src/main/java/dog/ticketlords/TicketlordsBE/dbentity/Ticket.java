package dog.ticketlords.TicketlordsBE.dbentity;

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

  // A booking site can have multiple tickets.
  @Setter
  @ManyToOne
  @JoinColumn(name = "ticket_vendor", referencedColumnName = "ticket_vendor")
  private BookingSite bookingSite;
  @Setter
  @Column(name = "ticket_link")
  private String ticketLink;
  @Setter
  @Column(name = "ticket_description")
  private String ticketDescription;

  /**
   * Constructs a ticket object.
   * 
   * @param ticketId          The id of an individual Ticket.
   * @param event             the event a ticket is for.
   * @param ticketType        The type of ticket; Normal, VIP etc...
   * @param price             The price of the ticket.
   * @param amountAvailable   The amount of tickets available.
   * @param bookingSite       the site at which said ticket can be purchased.
   * @param ticketLink        A link to the site where the ticket can be
   *                          purchased.
   * @param ticketDescription A description of the ticket.
   */
  public Ticket(int ticketId, Event event, String ticketType, BigDecimal price, int amountAvailable,
      BookingSite bookingSite, String ticketLink, String ticketDescription) {

    this.ticketId = ticketId;
    this.event = event;
    this.ticketType = ticketType;
    this.price = price;
    this.amountAvailable = amountAvailable;
    this.bookingSite = bookingSite;
    this.ticketLink = ticketLink;
    this.ticketDescription = ticketDescription;
  }

}
