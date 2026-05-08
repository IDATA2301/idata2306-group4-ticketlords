package dog.ticketlords.TicketlordsBE.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dog.ticketlords.TicketlordsBE.DTO.UserInterestScoreDTO;
import dog.ticketlords.TicketlordsBE.dbentity.Category;
import dog.ticketlords.TicketlordsBE.dbentity.UserInterest;
import dog.ticketlords.TicketlordsBE.repositories.UserInterestRepository;

@ExtendWith(MockitoExtension.class)
public class UserInterestServiceTests {

  @Mock
  private UserInterestRepository userInterestRepository;

  private UserInterestService userInterestService;
  private Clock testClock;

  @BeforeEach
  public void setUp() {
    testClock = Clock.fixed(Instant.parse("2026-04-15T19:05:31Z"), ZoneId.of("Europe/Oslo"));
    userInterestService = new UserInterestService(userInterestRepository, testClock);
  }

  /**
   * Tests that the DTO objects are created with the correct percentage based
   * values, between 0 and 1.
   */
  @Test
  public void userInterestCalculationReturnsCorrectResultPercentageBased() {

    Category sports = mock(Category.class);
    Category camelRiding = mock(Category.class);
    Category music = mock(Category.class);
    Category festival = mock(Category.class);
    Category cultural = mock(Category.class);

    when(sports.getCategoryId()).thenReturn(1L);
    when(camelRiding.getCategoryId()).thenReturn(2L);
    when(music.getCategoryId()).thenReturn(3L);
    when(festival.getCategoryId()).thenReturn(4L);
    when(cultural.getCategoryId()).thenReturn(5L);

    when(sports.getCategoryName()).thenReturn("Sports");
    when(camelRiding.getCategoryName()).thenReturn("Camel Riding");
    when(music.getCategoryName()).thenReturn("Music");
    when(festival.getCategoryName()).thenReturn("Festival");
    when(cultural.getCategoryName()).thenReturn("Cultural");

    UserInterest interest1 = mock(UserInterest.class);
    UserInterest interest2 = mock(UserInterest.class);
    UserInterest interest3 = mock(UserInterest.class);
    UserInterest interest4 = mock(UserInterest.class);
    UserInterest interest5 = mock(UserInterest.class);
    UserInterest interest6 = mock(UserInterest.class);
    UserInterest interest7 = mock(UserInterest.class);
    UserInterest interest8 = mock(UserInterest.class);
    UserInterest interest9 = mock(UserInterest.class);

    when(interest1.getClickedAt()).thenReturn(LocalDateTime.now(testClock).minusDays(90));
    when(interest2.getClickedAt()).thenReturn(LocalDateTime.now(testClock).minusDays(45));
    when(interest3.getClickedAt()).thenReturn(LocalDateTime.now(testClock).minusDays(3));
    when(interest4.getClickedAt()).thenReturn(LocalDateTime.now(testClock));
    when(interest5.getClickedAt()).thenReturn(LocalDateTime.now(testClock).minusDays(285));
    when(interest6.getClickedAt()).thenReturn(LocalDateTime.now(testClock).minusDays(70));
    when(interest7.getClickedAt()).thenReturn(LocalDateTime.now(testClock).minusDays(70));
    when(interest8.getClickedAt()).thenReturn(LocalDateTime.now(testClock));
    when(interest9.getClickedAt()).thenReturn(LocalDateTime.now(testClock).minusDays(60));

    when(interest1.getCategory()).thenReturn(sports);
    when(interest2.getCategory()).thenReturn(sports);
    when(interest3.getCategory()).thenReturn(sports);
    when(interest4.getCategory()).thenReturn(camelRiding);
    when(interest5.getCategory()).thenReturn(music);
    when(interest6.getCategory()).thenReturn(festival);
    when(interest7.getCategory()).thenReturn(festival);
    when(interest8.getCategory()).thenReturn(sports);
    when(interest9.getCategory()).thenReturn(cultural);

    List<UserInterest> interests = List.of(interest1, interest2, interest3, interest4, interest5, interest6, interest7,
        interest8, interest9);

    when(userInterestRepository.findByUser_UserId(1L)).thenReturn(interests);
    List<UserInterestScoreDTO> percentageBasedInterests = this.userInterestService
        .getAllCategoriesInterestScoreByUserSorted(1L);

    // Camel riding's score
    assertEquals(BigDecimal.valueOf(0.2351), percentageBasedInterests.get(0).getPercentageInterest());
    // Cultural's score
    assertEquals(BigDecimal.valueOf(0.0714), percentageBasedInterests.get(1).getPercentageInterest());
    // Festival's score
    assertEquals(BigDecimal.valueOf(0.1306), percentageBasedInterests.get(2).getPercentageInterest());
    // Music's score
    assertEquals(BigDecimal.valueOf(0.0098), percentageBasedInterests.get(3).getPercentageInterest());
    // Sport's score
    assertEquals(BigDecimal.valueOf(0.5531), percentageBasedInterests.get(4).getPercentageInterest());
  }
}
