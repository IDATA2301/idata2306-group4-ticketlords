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
  private int eventID;
  @Setter
  private String ticketType;
  @Setter
  private double price;
  @Setter
  private int amountAvailable;
  @Setter
  private String ticketVendor;
  @Setter
  private String ticketLink;
  @Setter
  private String ticketDescription;

  @Setter
  @ManyToOne
  @JoinColumn(name = "eventID", referencedColumnName = "eventID")
  private Event event;

  public Ticket(int ticketID, int eventID, String ticketType, double price, int amountAvailable,
      String ticketVendor, String ticketLink, String ticketDescription) {

    this.ticketID = ticketID;
    this.eventID = eventID;
    this.ticketType = ticketType;
    this.price = price;
    this.amountAvailable = amountAvailable;
    this.ticketVendor = ticketVendor;
    this.ticketLink = ticketLink;
    this.ticketDescription = ticketDescription;
  }

}
