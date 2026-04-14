package dog.ticketlords.TicketlordsBE.utility;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CategoryInterestMath {
  private static final int BASE_SAME_DAY_SCORE = 10;

  /**
   * Retrieves the strength of a specific interest, based on the date since said
   * interest was tracked.
   *
   * @param inputTime the amount of time passed since now.
   * @return a value between 10 and 0, depending on the date. If the date is in
   *         the future, or more than 365 days in the past, return 0.
   */
  public static BigDecimal getScoreByDate(LocalDateTime inputTime) {
    long daysSinceNow = ChronoUnit.DAYS.between(inputTime, LocalDateTime.now());
    if (daysSinceNow >= 365 || daysSinceNow < 0) {
      return BigDecimal.ZERO;
    } else {
      return BigDecimal
          .valueOf(BASE_SAME_DAY_SCORE * (scalingCalculator(ChronoUnit.DAYS.between(inputTime, LocalDateTime.now()))));
    }
  }

  /**
   * Calculates the scaling to apply to the {@BASE_SAME_DAY_SCORE} value.
   *
   * @param daysSinceNow a number representing the amount of days has passed since
   *                     now.
   * @return a scaling value between 0.0 and 1.0.
   */
  private static double scalingCalculator(long daysSinceNow) {
    return daysSinceNow * (-1 / (Math.log(366)) * Math.log(daysSinceNow + 1) + 1);
  }
}
