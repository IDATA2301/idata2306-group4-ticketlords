package dog.ticketlords.TicketlordsBE.DB;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter
public class EventVenue {

  @Id
  private int venueId;
  @Setter
  private String arena;
  @Setter
  private String city;
  @Setter
  private String country;
  @Setter
  private String address;

  public EventVenue(int venueId, String arena, String city, String country, String address) {
    this.venueId = venueId;
    this.address = address;
    this.arena = arena;
    this.country = country;
    this.city = city;
  }
}
