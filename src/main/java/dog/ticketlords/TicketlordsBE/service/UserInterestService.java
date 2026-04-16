package dog.ticketlords.TicketlordsBE.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dog.ticketlords.TicketlordsBE.DTO.UserInterestScoreDTO;
import dog.ticketlords.TicketlordsBE.dbentity.Category;
import dog.ticketlords.TicketlordsBE.dbentity.UserInterest;
import dog.ticketlords.TicketlordsBE.repositories.UserInterestRepository;
import dog.ticketlords.TicketlordsBE.utility.CategoryInterestMath;

public class UserInterestService {

  private UserInterestRepository userInterestRepository;
  private Clock clock;

  /**
   * Creates an instance of UserInterestService. The class takes a clock object,
   * which defines the time
   * the dependent parts of the class operate on. For most normal usecases this
   * should be Clock.systemDefaultZone();
   * For testing, the clock can be set to whatever is needed for the test.
   *
   * @param userInterestRepository dependency injection handled by spring boot.
   * @param clock
   */
  public UserInterestService(UserInterestRepository userInterestRepository, Clock clock) {
    this.userInterestRepository = userInterestRepository;
    this.clock = clock;
  }

  /**
   * Finds all {@link UserInterest} based on the user and category.
   *
   * @param userId       the user whose interest to find.
   * @param categoryName the name of the category to ...??
   *
   * @return A list of all {@link UserInterest}.
   */
  public List<UserInterest> getAllInterestRaw(long userId) {
    return this.userInterestRepository.findAllById(Collections.singleton(userId));

  }

  /**
   * Adds a {@link UserInterest} instance to the database, as long as it doesnt
   * already exist.
   *
   * @param userInterest the userInterest object to add.
   * @return true if successfull, and false otherwise.
   */
  public boolean addUserInterestEntry(UserInterest userInterest) {
    if (!this.userInterestRepository.existsById(userInterest.getId())) {
      this.userInterestRepository.save(userInterest);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Maps {@link UserInterest} by category, then calculates the
   * totalInterestScore, for this user in {@link CategoryInterestMath}.
   * Each category's interest score is calculated individually, and is set in each
   * {@link UserInterestScoreDTO} object, which represents
   * each category a user has a relation to. Before returning the completed list
   * of {@link UserInterestScoreDTO}, go over each object, and divide the score
   * it has, with the totalInterestScore variable, to normalize everything to
   * percentage values, giving a clean view over each category's
   * percentage interest, by a singular user.
   *
   * @param userId the id of the user to find the interests for.
   * @return A list of {@link UserInterestScoreDTO}, representing all the
   *         categories this user has shown interest in, percentage wise.
   */
  public List<UserInterestScoreDTO> getAllCategoriesInterestScoreByUser(long userId) {
    Map<Category, UserInterestScoreDTO> dtoMap = new HashMap<>();
    Map<Category, List<UserInterest>> groupedCategories = getSortedUserInterests(userId).stream()
        .collect(Collectors.groupingBy(UserInterest::getCategory));

    BigDecimal totalInterestScore = BigDecimal.ZERO;
    CategoryInterestMath mathHelper = new CategoryInterestMath(this.clock);

    for (Map.Entry<Category, List<UserInterest>> entry : groupedCategories.entrySet()) {
      Category category = entry.getKey();
      BigDecimal categoryScore = BigDecimal.ZERO;
      for (UserInterest interest : entry.getValue()) {
        BigDecimal score = mathHelper.getScoreByDate(interest.getClickedAt());
        categoryScore = categoryScore.add(score);
        totalInterestScore = totalInterestScore.add(score);
      }
      dtoMap.put(category, new UserInterestScoreDTO(userId, category.getCategoryName(), categoryScore));
    }
    for (UserInterestScoreDTO interest : dtoMap.values()) {
      if (totalInterestScore.compareTo(BigDecimal.ZERO) > 0) {
        interest.setPercentageInterest(
            interest.getPercentageInterest().divide(totalInterestScore, 4, RoundingMode.HALF_UP));
      } else {
        interest.setPercentageInterest(BigDecimal.ZERO);
      }
    }

    ArrayList<UserInterestScoreDTO> listOfUserInterestScoreDTOs = new ArrayList<>(dtoMap.values());
    return listOfUserInterestScoreDTOs;
  }

  /**
   * Sorts a list of {@link UserInterest} primarily by the category id, then the
   * date it's been clicked.
   */
  private List<UserInterest> getSortedUserInterests(long userId) {
    List<UserInterest> interests = userInterestRepository.findByUser_UserId(userId);
    return interests.stream().sorted(
        Comparator.comparing((UserInterest userInterest) -> userInterest
            .getCategory().getCategoryId())
            .thenComparing(UserInterest::getClickedAt))
        .collect(Collectors.toList());
  }

}
