package dog.ticketlords.TicketlordsBE.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.dbentity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
  List<Event> findByCategory_CategoryNameIgnoreCase(String categoryName);

  List<Event> findDistinctByEventNameContainingIgnoreCaseOrHostContainingIgnoreCaseOrCategory_CategoryNameContainingIgnoreCase(
      String searchTerm);

  long countByCategory_CategoryNameContainingIgnoreCase(String categoryNameSubstring);

  List<Event> findByHostIgnoreCase(String host);

  List<Event> findByEventNameContainingIgnoreCase(String name);

  List<Event> findByCategory_CategoryId(long Id);

  List<Event> findByEventDateStartBetween(LocalDateTime start, LocalDateTime end);

  List<Event> findByEventDateStartAfter(LocalDateTime date);

  List<Event> findByEventDateEndBefore(LocalDateTime date);
}
