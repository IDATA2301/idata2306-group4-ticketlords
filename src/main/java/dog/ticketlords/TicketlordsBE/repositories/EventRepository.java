package dog.ticketlords.TicketlordsBE.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import dog.ticketlords.TicketlordsBE.dbentity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
  List<Event> findByCategory_CategoryNameIgnoreCase(String categoryName);

  List<Event> findDistinctByEventNameContainingIgnoreCaseOrHostContainingIgnoreCaseOrCategory_CategoryNameContainingIgnoreCase(
      String search1, String search2, String search3);

  long countByCategory_CategoryNameContainingIgnoreCase(String categoryNameSubstring);

  List<Event> findByHostIgnoreCase(String host);

  List<Event> findByEventNameContainingIgnoreCase(String name);

  List<Event> findByCategory_CategoryId(long Id);

  List<Event> findByEventDateStartBetween(LocalDateTime start, LocalDateTime end);

  List<Event> findByEventDateStartAfter(LocalDateTime date);

  List<Event> findByEventDateEndBefore(LocalDateTime date);

  List<Event> findAllByOrderByTotalClicksDesc(Pageable pageable);

  List<Event> findByCategory_CategoryNameOrderByTotalClicksDesc(String categoryName, Pageable pageable);

  boolean existsByEventIdAndPubliclyVisibleIsTrue(long eventId);

  @Query("SELECT e.imgPathUrl FROM Event e WHERE e.eventId = :eventId")
  String findUrlById(@Param("eventId") long eventId);

  @Query("UPDATE Event e SET e.totalClicks = e.totalClicks + 1 WHERE e.eventId = :eventId")
  @Transactional
  @Modifying
  void incrementClickCount(@Param("eventId") long eventId);

  @Query("UPDATE Event e SET e.publiclyVisible = :publiclyVisible WHERE e.eventId = :eventId")
  @Transactional
  @Modifying
  void setPubliclyVisible(@Param("eventId") long eventId, @Param("publiclyVisible") boolean publiclyVisible);
}
