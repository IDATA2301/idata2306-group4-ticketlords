package dog.ticketlords.TicketlordsBE.DB;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
public class Category {
  @Id
  private int categoryID;
  @Setter
  private String categoryName;

  /**
   * This is for testing purposes only.
   * @param categoryID
   * @param categoryName
   */
  public Category(int categoryID, String categoryName) {
    this.categoryID = categoryID;
    this.categoryName = categoryName;
  }
}
