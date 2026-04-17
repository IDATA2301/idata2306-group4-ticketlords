package dog.ticketlords.TicketlordsBE.DTO;

import java.math.BigDecimal;

/**
 * Represents a user's interest score for a specific category.
 * The interest is given by a decimal value between 0.0 and 1.0,
 * where higher values indicate stronger interest compared to other categories.
 *
 * @param userId             the unique identifier of the user.
 * @param categoryName       the name of the category
 * @param percentageInterest the user's interest in the category (0.00 to 1.00)
 */
public class UserInterestScoreDTO {

  private long userId;
  private String categoryName;
  private BigDecimal percentageInterest;

  public UserInterestScoreDTO(long userId, String categoryName, BigDecimal percentageInterest) {
    this.userId = userId;
    this.categoryName = categoryName;
    this.percentageInterest = percentageInterest;
  }

  public long getUserId() {
    return this.userId;
  }

  public String getCategoryName() {
    return this.categoryName;
  }

  public BigDecimal getPercentageInterest() {
    return this.percentageInterest;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public void setPercentageInterest(BigDecimal percentageInterest) {
    this.percentageInterest = percentageInterest;
  }
}
