package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.dbentity.PriceAlert;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
}
