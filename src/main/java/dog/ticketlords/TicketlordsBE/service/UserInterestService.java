package dog.ticketlords.TicketlordsBE.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dog.ticketlords.TicketlordsBE.dbentity.Category;
import dog.ticketlords.TicketlordsBE.DTO.UserInterestScore;
import dog.ticketlords.TicketlordsBE.dbentity.UserInterest;
import dog.ticketlords.TicketlordsBE.repositories.UserInterestRepository;

public class UserInterestService {

  private UserInterestRepository userInterestRepository;

  public UserInterestService(UserInterestRepository userInterestRepository) {
    this.userInterestRepository = userInterestRepository;
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

  public List<UserInterestScore> getAllCategoriesInterestScoreByUser(long userId) {
    Map<Category, List<UserInterest>> groupedCategories = getSortedUserInterests(userId).stream()
        .collect(Collectors.groupingBy(UserInterest::getCategory));

    // TODO: Placeholder. Run through actual algorithm, then return correct list.
    return List.of(new UserInterestScore(1, "Placeholder", new BigDecimal("1.00")));

  }

  private List<UserInterest> getSortedUserInterests(long userId) {
    List<UserInterest> interests = userInterestRepository.findByUser_UserId(userId);
    return interests.stream().sorted(
        Comparator.comparing((UserInterest userInterest) -> userInterest
            .getCategory().getCategoryId())
            .thenComparing(UserInterest::getClickedAt))
        .collect(Collectors.toList());
  }

}
