package dog.ticketlords.TicketlordsBE.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.dbentity.SearchLog;
import dog.ticketlords.TicketlordsBE.dbentity.UnregisteredUser;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {

  List<SearchLog> findTopNByUser_UIdOrderBySearchedAtDesc(long userId, Pageable pageable);

}
