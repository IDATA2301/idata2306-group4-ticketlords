package dog.ticketlords.TicketlordsBE.dbentity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representation of the database event_venue table.
 * Represents an event_venue with location details and an id.
 */

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "event_venue")
public class EventVenue {

  @Id
  @Column(name = "venue_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long venueId;
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

  /**
   * Constructs an EventVenue object.
   *
   * @param venueId The id of the venue.
   * @param arena   the arena of the venue.
   * @param city    the city the venue is held.
   * @param country The country the venue is held in.
   * @param address The address of the venue.
   */
  public EventVenue(int venueId, String arena, String city, String country, String address) {
    this.venueId = venueId;
    this.address = address;
    this.arena = arena;
    this.country = country;
    this.city = city;
  }
}
