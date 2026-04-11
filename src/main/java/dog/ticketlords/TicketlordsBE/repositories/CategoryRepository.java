package dog.ticketlords.TicketlordsBE.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.dbentity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByCategoryName(String categoryName);

  List<Category> findByCategoryNameContainingIgnoreCase(String substringCategoryName);
}
