package dog.ticketlords.TicketlordsBE.DB;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "event")
public class Event {

  @Id
  @Column(name = "event_id")
  private int eventID;
  @Setter
  @Column(name = "event_name", nullable = false)
  private String eventName;
  @Setter
  @Column(name = "host")
  private String host;
  @ManyToOne
  @JoinColumn(name = "category_id", referencedColumnName = "category_id")
  @Column(name = "category")
  @Setter
  private Category category;
  @Setter
  @Column(name = "event_date_start")
  private LocalDate eventDateStart;
  @Setter
  @Column(name = "event_date_end")
  private LocalDate eventDateEnd;
  @Setter
  @ManyToOne
  @JoinColumn(name = "venue_id", referencedColumnName = "venue_id")
  @Column(name = "event_venue")
  private EventVenue eventVenue;
  @Setter
  @Column(name = "event_description")
  private String eventDescription;
  @Setter
  @Column(name = "total_clicks", nullable = false)
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
