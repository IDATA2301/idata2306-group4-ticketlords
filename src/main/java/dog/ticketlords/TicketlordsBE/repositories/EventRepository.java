package dog.ticketlords.TicketlordsBE.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
  List<Event> findByCategory_CategoryNameIgnoreCase(String categoryName);

  List<Event> findByHost(String host);

  List<Event> findByNameContaining(String name);

  List<Event> findByCategory_CategoryId(long Id);

  List<Event> findByEventDateStartBetween(LocalDateTime start, LocalDateTime end);

  List<Event> findByEventDateStartAfter(LocalDateTime date);
}
