package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.entity.PriceAlert;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Integer> {
}
