package dog.ticketlords.TicketlordsBE.dbentity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representation of the database booking_site table.
 * Represents a BookingSite with vendor, website link and site description.
 */

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "booking_site")
public class BookingSite {

  @Id
  @Column(name = "ticket_vendor_id")
  private long ticketVendorId;
  @Setter
  @Column(name = "ticket_vendor")
  private String ticketVendor;
  @Setter
  @Column(name = "website_link")
  private String websiteLink;
  @Setter
  @Column(name = "booking_site_description")
  private String bookingSiteDescription;

  /**
   * Creates an instance of BookingSite.
   *
   * @param ticketVendorId         the id of vendor that sells a specific ticket.
   * @param ticketVendor           the vendor that sells a specific ticket.
   * @param websiteLink            the link to the vendor's website.
   * @param bookingSiteDescription description of the website.
   */
  public BookingSite(long ticketVendorId, String ticketVendor, String websiteLink, String bookingSiteDescription) {
    this.ticketVendorId = ticketVendorId;
    this.ticketVendor = ticketVendor;
    this.websiteLink = websiteLink;
    this.bookingSiteDescription = bookingSiteDescription;
  }
}
