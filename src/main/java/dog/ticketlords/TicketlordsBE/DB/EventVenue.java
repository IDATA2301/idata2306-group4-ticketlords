package dog.ticketlords.TicketlordsBE.DB;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "event_venue")
public class EventVenue {

  @Id
  @Column(name = "venue_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int venueId;
  @Setter
  @Column(name = "arena", nullable = false)
  private String arena;
  @Setter
  @Column(name = "city")
  private String city;
  @Setter
  @Column(name = "country", nullable = false)
  private String country;
  @Setter
  @Column(name = "address", nullable = false)
  private String address;

  public EventVenue(int venueId, String arena, String city, String country, String address) {
    this.venueId = venueId;
    this.address = address;
    this.arena = arena;
    this.country = country;
    this.city = city;
  }
}
