package dog.ticketlords.TicketlordsBE.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dog.ticketlords.TicketlordsBE.dbentity.BookingSite;
import dog.ticketlords.TicketlordsBE.dbentity.Review;
import dog.ticketlords.TicketlordsBE.entity.VendorRating;
import dog.ticketlords.TicketlordsBE.repositories.ReviewRepository;

public class ReviewServiceTests {

  private ReviewRepository reviewRepo;
  private ReviewService reviewService;

  @BeforeEach
  public void setUp() {
    reviewRepo = mock(ReviewRepository.class);
    reviewService = new ReviewService(reviewRepo);
  }

  @Test
  public void testGetAverageRatingForAllVendorsByName() {
    Review review1 = mock(Review.class);
    Review review2 = mock(Review.class);
    Review review3 = mock(Review.class);
    Review review4 = mock(Review.class);

    BookingSite site1 = mock(BookingSite.class);
    BookingSite site2 = mock(BookingSite.class);

    when(site1.getTicketVendor()).thenReturn("VendorA");
    when(site2.getTicketVendor()).thenReturn("VendorB");

    when(review1.getBookingSite()).thenReturn(site1);
    when(review2.getBookingSite()).thenReturn(site1);
    when(review3.getBookingSite()).thenReturn(site2);
    when(review4.getBookingSite()).thenReturn(site1);

    when(review1.getScore()).thenReturn(4.0);
    when(review2.getScore()).thenReturn(3.0);
    when(review3.getScore()).thenReturn(5.0);
    when(review4.getScore()).thenReturn(3.0);
    List<Review> reviews = Arrays.asList(review1, review2, review3, review4);
    when(reviewRepo.findAllByBookingSite_TicketVendorIgnoreCaseContaining("Vendor"))
        .thenReturn(reviews);

    ReviewService service = new ReviewService(reviewRepo);
    List<VendorRating> result = reviewService.getAverageRatingForAllVendorsByName("Vendor");
    System.out.println(result.get(0).vendorName() + " " + result.get(0).avgVendorRating());
    System.out.println(result.get(1).vendorName() + " " + result.get(1).avgVendorRating());
    assertEquals(2, result.size());
    assertTrue(result.stream().anyMatch(vr -> vr.vendorName().equals("VendorA") && vr.avgVendorRating() == 3.33333333333333333));
    assertTrue(result.stream().anyMatch(vr -> vr.vendorName().equals("VendorB") && vr.avgVendorRating() == 5.0));

  }
}
