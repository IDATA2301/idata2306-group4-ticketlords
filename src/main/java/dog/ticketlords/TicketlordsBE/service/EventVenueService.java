package dog.ticketlords.TicketlordsBE.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import dog.ticketlords.TicketlordsBE.dbentity.EventVenue;
import dog.ticketlords.TicketlordsBE.repositories.EventVenueRepository;

@Service
public class EventVenueService {

  private EventVenueRepository eventVenueRepo;

  public EventVenueService(EventVenueRepository eventVenueRepo) {
    this.eventVenueRepo = eventVenueRepo;
  }

  public Optional<EventVenue> getEventVenueById(long eId) {
    return this.eventVenueRepo.findById(eId);
  }

  public List<EventVenue> getEventVenueByLocation(String country, String city) {
    return this.eventVenueRepo.findAllByCountryIgnoreCaseAndCityIgnoreCase(country, city);
  }

  public boolean addEventVenueToDatabase(EventVenue eventVenue) {
    Example<EventVenue> example = Example.of(eventVenue);
    if (!this.eventVenueRepo.exists(example)) {
      this.eventVenueRepo.save(eventVenue);
      return true;
    } else {
      return false;
    }
  }

  public Optional<EventVenue> getVenueByAddress(String venueAddress) {
    return this.eventVenueRepo.findByAddressIgnoreCase(venueAddress);
  }

  public boolean updateEventVenue(EventVenue updatedEVenue) {
    if (this.eventVenueRepo.existsById(updatedEVenue.getVenueId())) {
      EventVenue databaseEVenue = this.eventVenueRepo.getReferenceById(updatedEVenue.getVenueId());
      if (updatedEVenue.getAddress() != null) {
        databaseEVenue.setAddress(updatedEVenue.getAddress());
      }
      if (updatedEVenue.getArena() != null) {
        databaseEVenue.setArena(updatedEVenue.getArena());
      }
      if (updatedEVenue.getCity() != null) {
        databaseEVenue.setCity(updatedEVenue.getCity());
      }
      if (updatedEVenue.getCountry() != null) {
        databaseEVenue.setCountry(updatedEVenue.getCountry());
      }
      return true;
    } else {
      return false;
    }
  }
}
