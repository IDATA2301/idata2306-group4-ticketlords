package dog.ticketlords.TicketlordsBE.dbentity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representation of the database event table.
 * Represents an event with relevant info about the event.
 */

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "event")
public class Event {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "event_id", insertable = false, updatable = false)
  @Schema(accessMode = Schema.AccessMode.READ_ONLY, hidden = true)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private long eventId;
  @Setter
  @Column(name = "event_name", nullable = false)
  private String eventName;
  @Setter
  @Column(name = "host")
  private String host;
  @ManyToOne
  @JoinColumn(name = "category_id", referencedColumnName = "category_id")
  @Setter
  private Category category;
  @Setter
  @Column(name = "event_date_start")
  private LocalDateTime eventDateStart;
  @Setter
  @Column(name = "event_date_end")
  private LocalDateTime eventDateEnd;
  @Setter
  @ManyToOne
  @JoinColumn(name = "venue_id", referencedColumnName = "venue_id")
  private EventVenue eventVenue;
  @Setter
  @Column(name = "event_description")
  private String eventDescription;
  @Setter
  @Column(name = "total_clicks", nullable = false)
  private long totalClicks;

  // Of form: images/imgName.jpg
  @Setter
  @Column(name = "image_url", nullable = true)
  private String imgPathUrl;

  @Setter
  @Column(name = "publicly_visible", nullable = false)
  private boolean publiclyVisible;

  /**
   * Constructs an Event with all fields.
   *
   * @param eventName        the name of the event.
   * @param host             the host of the event.
   * @param category         the {@link Category} the event belongs to.
   * @param eVenue           the {@link EventVenue} where the event takes place.
   * @param eventDescription a description of the event.
   * @param totalClicks      the total number of clicks/views.
   * @param start            the start date of the event.
   * @param end              the end date of the event.
   */
  public Event(String eventName, String host, Category category, EventVenue eVenue,
      String eventDescription, long totalClicks, LocalDateTime start, LocalDateTime end, String imgPathUrl) {
    this.eventName = eventName;
    this.host = host;
    this.category = category;
    this.eventDateStart = start;
    this.eventDateEnd = end;
    this.eventVenue = eVenue;
    this.eventDescription = eventDescription;
    this.totalClicks = totalClicks;
    this.imgPathUrl = imgPathUrl;
  }
}
