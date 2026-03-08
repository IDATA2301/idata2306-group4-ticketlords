package dog.ticketlords.TicketlordsBE.DB;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
  @ManyToOne
  @JoinColumn(name = "category_id", referencedColumnName = "category_id")
  @Setter
  private Category category;
  @Setter
  private LocalDate eventDateStart;
  @Setter
  private LocalDate eventDateEnd;
  @Setter
  @ManyToOne
  @JoinColumn(name = "venue_id", referencedColumnName = "venue_id")
  private EventVenue eventVenue;
  @Setter
  private String eventDescription;
  @Setter
  private int totalClicks;

  public Event(String eventName, int eventID, String host, Category category, EventVenue eVenue,
      String eventDescription, int totalClicks, LocalDate start, LocalDate end) {
    this.eventName = eventName;
    this.eventID = eventID;
    this.host = host;
    this.category = category;
    this.eventDateStart = start;
    this.eventDateEnd = end;
    this.eventVenue = eVenue;
    this.eventDescription = eventDescription;
    this.totalClicks = totalClicks;

  }
}
