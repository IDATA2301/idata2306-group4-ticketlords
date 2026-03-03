package dog.ticketlords.TicketlordsBE.DB;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
public class BookingSite {

  @Id
  private String ticketVendor;
  @Setter
  private String websiteLink;
  @Setter
  private String bookingSiteDescription;

  public BookingSite(String ticketVendor, String websiteLink, String bookingSiteDescription) {
    this.ticketVendor = ticketVendor;
    this.websiteLink = websiteLink;
    this.bookingSiteDescription = bookingSiteDescription;
  }
}
