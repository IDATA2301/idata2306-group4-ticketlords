package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.entity.SearchLog;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {
}
