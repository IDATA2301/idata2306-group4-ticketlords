package dog.ticketlords.TicketlordsBE.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dog.ticketlords.TicketlordsBE.dbentity.EventClicks;
import dog.ticketlords.TicketlordsBE.dbentity.EventClicksId;
import dog.ticketlords.TicketlordsBE.DTO.CategoryClicksDTO;

public interface EventClicksRepository extends JpaRepository<EventClicks, EventClicksId> {
  long countByEvent_EventId(long eventId);

  List<EventClicks> findAllByEvent_EventId(long eventId);

  @Query("SELECT new dog.ticketlords.TicketlordsBE.DTO.CategoryClicksDTO(ec.user.uId, c.categoryId, c.categoryName, COUNT(ec)) "
      +
      "FROM EventClicks ec " +
      "JOIN ec.event e " +
      "JOIN e.category c " +
      "WHERE ec.user.uId = :userId " +
      "GROUP BY ec.user.uId, c.categoryId, c.categoryName " +
      "ORDER BY COUNT(ec) DESC")
  List<CategoryClicksDTO> findClicksByUserPerCategory(@Param("userId") long userId);

  boolean existsByEvent_EventId(long eventId);

  @Query("SELECT ec.lastInteraction FROM EventClicks ec WHERE ec.event.eventId = :eventId AND ec.user.uId = :userId")
  LocalDateTime findLastInteraction(@Param("eventId") long eventId, @Param("userId") long userId);
}
