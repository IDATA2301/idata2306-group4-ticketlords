package dog.ticketlords.TicketlordsBE.DB;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Composite primary key for the {@link UserInterest} entity,
 * consisting of a user ID and a category name.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class UserInterestId implements Serializable {

  private int userId;
  private String categoryName;

  /**
   * Constructs an instance of UserInterestId. Should only be used for testing.
   *
   * @param userId       the user ID.
   * @param categoryName the category name.
   */
  public UserInterestId(int userId, String categoryName) {
    this.userId = userId;
    this.categoryName = categoryName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof UserInterestId))
      return false;
    UserInterestId that = (UserInterestId) o;
    return userId == that.userId && Objects.equals(categoryName, that.categoryName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, categoryName);
  }
}
