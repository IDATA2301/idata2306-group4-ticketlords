package dog.ticketlords.TicketlordsBE.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.dbentity.EventVenue;

public interface EventVenueRepository extends JpaRepository<EventVenue, Long> {

  List<EventVenue> findAllByCountryIgnoreCaseAndCityIgnoreCase(String country, String city);

  List<EventVenue> findByAddressIgnoreCase(String address);
  List<EventVenue> findByArenaIgnoreCase(String arena);
  List<EventVenue> findByCityIgnoreCase(String city);
  List<EventVenue> findByCountryIgnoreCase(String country);

  Optional<EventVenue> findByAddressIgnoreCaseAndArenaIgnoreCaseAndCityIgnoreCaseAndCountryIgnoreCase(String address,
      String arena, String city, String country);
}
