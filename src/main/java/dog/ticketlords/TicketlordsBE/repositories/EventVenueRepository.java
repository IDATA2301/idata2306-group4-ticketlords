package dog.ticketlords.TicketlordsBE.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.dbentity.EventVenue;

public interface EventVenueRepository extends JpaRepository<EventVenue, Long> {

  List<EventVenue> findAllByCountryIgnoreCaseAndCityIgnoreCase(String country, String city);

  Optional<EventVenue> findByAddressIgnoreCase(String venueAddress);
}
