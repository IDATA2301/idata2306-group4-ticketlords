package dog.ticketlords.TicketlordsBE.dbentity;

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
import lombok.Setter;

/**
 * Entity representation of the database category table.
 * Represents a category with an id and a name.
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id")
  @Schema(accessMode = Schema.AccessMode.READ_ONLY, hidden = true)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private long categoryId;
  @Setter
  @Column(name = "category_name", nullable = false, unique = true)
  private String categoryName;

  /**
   * Creates an instance of Category.
   *
   * @param categoryName the name of the category.
   */
  public Category(String categoryName) {
    this.categoryName = categoryName;

  }

  @Override
  public String toString() {
    return "Category [categoryId=" + categoryId + ", categoryName=" + categoryName + "]";
  }

}
