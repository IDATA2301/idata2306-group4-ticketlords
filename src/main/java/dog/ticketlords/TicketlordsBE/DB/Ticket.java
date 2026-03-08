package dog.ticketlords.TicketlordsBE.DB;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "ticket")
public class Ticket {

  @Id
  @Column(name = "ticket_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int ticketId;
  @Setter
  @ManyToOne
  @JoinColumn(name = "event_id", referencedColumnName = "event_id")
  @Column(name = "event_id")
  private Event event;
  @Setter
  @Column(name = "ticket_type")
  private String ticketType;
  @Setter
  @Column(name = "price", nullable = false)
  private double price;
  @Setter
  @Column(name = "amount_available", nullable = false)
  private int amountAvailable;
  @Setter
  @ManyToOne
  @JoinColumn(name = "ticket_vendor", referencedColumnName = "ticket_vendor")
  @Column(name = "booking_site")
  private BookingSite bookingSite;
  @Setter
  @Column(name = "ticket_link")
  private String ticketLink;
  @Setter
  @Column(name = "ticket_description")
  private String ticketDescription;

  public Ticket(int ticketId, Event event, String ticketType, double price, int amountAvailable,
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
