package dog.ticketlords.TicketlordsBE.DB;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
public class Ticket {

  @Id
  private int ticketID;
  @Setter
  @ManyToOne
  @JoinColumn(name = "event_id", referencedColumnName = "event_id")
  private Event event;
  @Setter
  private String ticketType;
  @Setter
  private double price;
  @Setter
  private int amountAvailable;
  @Setter
  @ManyToOne
  @JoinColumn(name = "ticket_vendor", referencedColumnName = "ticket_vendor")
  private BookingSite bookingSite;
  @Setter
  private String ticketLink;
  @Setter
  private String ticketDescription;

  public Ticket(int ticketID, Event event, String ticketType, double price, int amountAvailable,
      BookingSite bookingSite, String ticketLink, String ticketDescription) {

    this.ticketID = ticketID;
    this.event = event;
    this.ticketType = ticketType;
    this.price = price;
    this.amountAvailable = amountAvailable;
    this.bookingSite = bookingSite;
    this.ticketLink = ticketLink;
    this.ticketDescription = ticketDescription;
  }

}
