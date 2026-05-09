package dog.ticketlords.TicketlordsBE.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import dog.ticketlords.TicketlordsBE.DTO.RecommendedEventsDTO;
import dog.ticketlords.TicketlordsBE.DTO.UserInterestScoreDTO;
import dog.ticketlords.TicketlordsBE.dbentity.Category;
import dog.ticketlords.TicketlordsBE.dbentity.Event;
import dog.ticketlords.TicketlordsBE.dbentity.UserInterest;
import dog.ticketlords.TicketlordsBE.repositories.CategoryRepository;
import dog.ticketlords.TicketlordsBE.repositories.UserInterestRepository;
import dog.ticketlords.TicketlordsBE.utility.CategoryInterestMath;
import dog.ticketlords.TicketlordsBE.repositories.EventRepository;

/**
 * Service class for service logic.
 */
@Service
public class UserInterestService {

  private UserInterestRepository userInterestRepository;
  private Clock clock;
  private CategoryRepository categoryRepository;
  private EventRepository eventRepository;

  /**
   * Creates an instance of UserInterestService. The class takes a clock object,
   * which defines the time
   * the dependent parts of the class operate on. For most normal usecases this
   * should be Clock.systemDefaultZone();
   * For testing, the clock can be set to whatever is needed for the test.
   *
   * @param userInterestRepository dependency injection handled by spring boot.
   * @param clock                  clock used mainly for testing purposes.
   */
  public UserInterestService(UserInterestRepository userInterestRepository, CategoryRepository categoryRepository,
      EventRepository eventRepository, Clock clock) {
    this.userInterestRepository = userInterestRepository;
    this.categoryRepository = categoryRepository;
    this.eventRepository = eventRepository;
    this.clock = clock;
  }

  /**
   * Finds all {@link UserInterest} based on the user.
   *
   * @param userId the user whose interest to find.
   *
   * @return A list of all {@link UserInterest} sorted newest first -> oldest
   *         last.
   */
  public List<UserInterest> getAllInterestRaw(long userId) {
    List<UserInterest> interests = this.userInterestRepository.findByUser_UserId(userId);
    interests.sort(Comparator.comparing(UserInterest::getClickedAt).reversed());
    return interests;
  }

  /**
   * Adds a {@link UserInterest} instance to the database, as long as it doesnt
   * already exist, or if they have not clicked on the same category within 10
   * seconds.
   *
   * @param userInterest the userInterest object to add.
   * @return true if successful, and false otherwise.
   */
  public boolean addUserInterestEntry(UserInterest userInterest) {
    Optional<LocalDateTime> latestInterestEntry = this.userInterestRepository.findMostRecentInterestByUserAndCategory(
        userInterest.getUser().getUserId(), userInterest.getCategory().getCategoryId());
    if (latestInterestEntry.isEmpty()) {
      this.userInterestRepository.save(userInterest);
      return true;
    } else if (ChronoUnit.SECONDS.between(latestInterestEntry.get(), LocalDateTime.now(clock)) >= 10) {
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
   *         categories this user has shown interest in, percentage wise. The list
   *         is sorted by the cateories names.
   */
  public List<UserInterestScoreDTO> getAllCategoriesInterestScoreByUserSorted(long userId) {
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
    listOfUserInterestScoreDTOs.sort(Comparator.comparing(interest -> interest.getCategoryName()));
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

  /**
   * Finds 5 events to recommend a user, based on their categorical interests, and
   * each category's most popular events.
   * The algorithm uses {@link Random} to find a highly rated event, but not the
   * same every time, for variety.
   * 
   * If a user has many categorical interests, this method will return 5 events of
   * differing categories, however if
   * the user has few categorical interests, it may return multiple events of the
   * same category.
   *
   * @param userId the user to find interests for.
   * @return A {@link RecommendedEventsDTO} holding the {@link Event}s to
   *         recommend the user.
   */
  public RecommendedEventsDTO getRecommendedEvents(long userId) {
    List<UserInterestScoreDTO> interests = getAllCategoriesInterestScoreByUserSorted(userId);
    double interestAccumulator = 0.0;
    double interestThreshold = 0.7;
    List<Category> topCategoricalInterests = new ArrayList<>();
    if (interests.isEmpty()) {
      return new RecommendedEventsDTO(new ArrayList<Event>());
    }

    for (UserInterestScoreDTO interest : interests) {
      if ((interestAccumulator + interest.getPercentageInterest().doubleValue()) >= interestThreshold) {
        break;
      }

      if (topCategoricalInterests.size() >= 5) {
        break;
      }
      interestAccumulator += interest.getPercentageInterest().doubleValue();
      this.categoryRepository.findByCategoryName(interest.getCategoryName()).ifPresent(topCategoricalInterests::add);
    }

    // At this point interests hold either top 70% categorical interests, or top 5
    // categorical interests.

    if (topCategoricalInterests.isEmpty()) {
      return new RecommendedEventsDTO(new ArrayList<Event>());
    }

    List<Event> recommendedEvents = new ArrayList<>();
    Random random = new Random();
    int target = 4; // Events to get
    int attempts = 0;
    int maxAttempts = topCategoricalInterests.size() * 3;

    while (recommendedEvents.size() < target && attempts < maxAttempts) {
      Category category = topCategoricalInterests.get(attempts % topCategoricalInterests.size());
      List<Event> topCategoryEvents = this.eventRepository
          .findByCategory_CategoryNameOrderByTotalClicksDesc(category.getCategoryName(), PageRequest.of(0, 20));

      if (!topCategoryEvents.isEmpty()) {
        recommendedEvents.add(topCategoryEvents.get(random.nextInt(topCategoryEvents.size())));
      }
      attempts++;
    }
    return new RecommendedEventsDTO(recommendedEvents);
  }
}
