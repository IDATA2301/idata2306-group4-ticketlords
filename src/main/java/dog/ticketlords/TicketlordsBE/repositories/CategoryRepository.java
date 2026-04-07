package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.dbentity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
