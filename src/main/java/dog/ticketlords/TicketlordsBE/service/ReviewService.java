package dog.ticketlords.TicketlordsBE.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dog.ticketlords.TicketlordsBE.repositories.ReviewRepository;


@Service
@Transactional
public class ReviewService {

  private final ReviewRepository revRepo;

  /**
   * Creates a new Service for Review operations.
   * 
   * @param revRepo the repository to perform persistence operations for {@link Review}
   */
  public ReviewService(ReviewRepository revRepo) {
    this.revRepo = revRepo;
  }

  //TODO: finish
  @Transactional(readOnly = true)
  public Optional<Review> getReview()


}
