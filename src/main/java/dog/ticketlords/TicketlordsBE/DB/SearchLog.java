package dog.ticketlords.TicketlordsBE.DB;

import java.time.LocalDateTime;

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
 * Entity representation of the database search_log table.
 * Represents a search query made by any user, registered or not,
 * storing the query text and timestamp.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "search_log")
public class SearchLog {

  /** The unique identifier of the search log entry. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "search_id")
  private int searchId;

  /** The user who performed the search. */
  @Setter
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private UnregisteredUser user;

  /** The search query text. */
  @Setter
  @Column(name = "search_query", nullable = false)
  private String searchQuery;

  /** The timestamp when the search was performed. */
  @Setter
  @Column(name = "searched_at", insertable = false, updatable = false)
  private LocalDateTime searchedAt;

  /**
   * Constructs a SearchLog. Should only be used for testing.
   *
   * @param user        the {@link UnregisteredUser} who performed the search.
   * @param searchQuery the search query text.
   * @param searchedAt  the timestamp of the search.
   */
  public SearchLog(UnregisteredUser user, String searchQuery, LocalDateTime searchedAt) {
    this.user = user;
    this.searchQuery = searchQuery;
    this.searchedAt = searchedAt;
  }
}
