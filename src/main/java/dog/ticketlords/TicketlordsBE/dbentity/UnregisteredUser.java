package dog.ticketlords.TicketlordsBE.dbentity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents any arbitrary user without a registered account in the system.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "unregistered_user")
public class UnregisteredUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", insertable = false, updatable = false)
  @Schema(accessMode = Schema.AccessMode.READ_ONLY, hidden = true)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long uId;

  @Column(name = "first_seen", insertable = false, updatable = false)
  private LocalDateTime firstSeen;

  @Override
  public boolean equals(Object o) {
    boolean doesEquals = false;
    if (this == o) {
      doesEquals = true;
    }
    if (o instanceof UnregisteredUser) {
      UnregisteredUser that = (UnregisteredUser) o;
      if (this.uId == that.getUId()) {
        doesEquals = true;
      }
    }
    return doesEquals;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(uId);
  }

}
