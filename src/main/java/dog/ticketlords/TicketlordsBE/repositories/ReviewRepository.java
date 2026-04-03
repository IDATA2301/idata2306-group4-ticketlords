package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.entity.Review;
import dog.ticketlords.TicketlordsBE.entity.ReviewId;

public interface ReviewRepository extends JpaRepository<Review, ReviewId> {
}
