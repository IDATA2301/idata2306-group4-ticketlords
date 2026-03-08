package dog.ticketlords.TicketlordsBE.DB;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Category {

  @Id
  private int venueId;
  @Setter
  private String categoryName;

  public Category(int venueId, String categoryName) {
    this.categoryName = categoryName;
    this.venueId = venueId;

  }
}
