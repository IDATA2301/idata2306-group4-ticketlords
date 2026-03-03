package dog.ticketlords.TicketlordsBE.DB;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter
public class Event {

  @Id
  private int eventID;
  @Setter
  private String eventName;
  @Setter
  private String host;
  @Setter
  private String category;
  @Setter
  private String country;
  @Setter
  private String city;
  @Setter
  private String arena;
  @Setter
  private String eventDescription;
  @Setter
  private int totalClicks;
  @Setter
  private LocalDate eventDateStart;
  @Setter
  private LocalDate eventDateEnd;

  public Event(String eventName, int eventID, String host, String category, String country, String city, String arena,
      String eventDescription, int totalClicks, LocalDate start, LocalDate end) {
    this.eventName = eventName;
    this.eventID = eventID;
    this.host = host;
    this.category = category;
    this.country = country;
    this.city = city;
    this.arena = arena;
    this.eventDescription = eventDescription;
    this.totalClicks = totalClicks; // TODO: Should just be default 0 at creation?
    this.eventDateStart = start;
    this.eventDateEnd = end;

  }
}
