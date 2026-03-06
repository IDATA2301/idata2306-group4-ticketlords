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
  private int categoryID;
  @Setter
  private LocalDate eventDateStart;
  @Setter
  private LocalDate eventDateEnd;
  @Setter
  private int venueID;
  @Setter
  private String eventDescription;
  @Setter
  private int totalClicks;

  public Event(String eventName, int eventID, String host, int categoryID, int venueID,
      String eventDescription, int totalClicks, LocalDate start, LocalDate end) {
    this.eventName = eventName;
    this.eventID = eventID;
    this.host = host;
    this.categoryID = categoryID;
    this.eventDateStart = start;
    this.eventDateEnd = end;
    this.venueID = venueID;
    this.eventDescription = eventDescription;
    this.totalClicks = totalClicks;

  }
}
