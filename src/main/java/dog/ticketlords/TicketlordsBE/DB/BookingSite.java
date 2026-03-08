package dog.ticketlords.TicketlordsBE.DB;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "booking_site")
public class BookingSite {

  @Id
  @Column(name = "ticket_vendor")
  private String ticketVendor;
  @Setter
  @Column(name = "website_link")
  private String websiteLink;
  @Setter
  @Column(name = "booking_site_description")
  private String bookingSiteDescription;

  public BookingSite(String ticketVendor, String websiteLink, String bookingSiteDescription) {
    this.ticketVendor = ticketVendor;
    this.websiteLink = websiteLink;
    this.bookingSiteDescription = bookingSiteDescription;
  }
}
