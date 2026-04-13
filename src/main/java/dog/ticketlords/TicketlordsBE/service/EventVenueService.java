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

  /**
   * Gets an optional of an event venue based on its id.
   *
   * @param eId the id of the {@link EventVenue} to find.
   * @return an optional containing the EventVenue if it exists, optional
   *         containing null otherwise.
   */
  public Optional<EventVenue> getEventVenueById(long eId) {
    return this.eventVenueRepo.findById(eId);
  }

  /**
   * Gets a list of {@link EventVenue} based on the country and the city they are
   * located.
   *
   * @param country the country the EventVenue is located.
   * @param city    the city the EventVenue is located.
   *
   * @return A list of all EventVenues matching whose country and city match the
   *         params. If none match, return an empty list.
   */
  public List<EventVenue> getEventVenueByLocation(String country, String city) {
    return this.eventVenueRepo.findAllByCountryIgnoreCaseAndCityIgnoreCase(country, city);
  }

  /**
   * Adds an {@link EventVenue} to the database.
   *
   * @param an {@link EventVenue} object to insert to the database.
   * @return true if successfully added. False otherwise.
   */
  public boolean addEventVenueToDatabase(EventVenue eventVenue) {
    Example<EventVenue> example = Example.of(eventVenue);
    if (!this.eventVenueRepo.exists(example)) {
      this.eventVenueRepo.save(eventVenue);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Gets an optional of an {@link EventVenue} based on its address.
   *
   * @param venueAddress the address where the venue is located.
   * @return An optional containing the EventVenue if it exists, optional
   *         containing null otherwise.
   */
  public Optional<EventVenue> getVenueByAddress(String venueAddress) {
    return this.eventVenueRepo.findByAddressIgnoreCase(venueAddress);
  }

  /**
   * Gets an {@link EventVenue} reference from the database based on an incoming
   * EventVenue's id field.
   * Then updates the database event venue reference with the incoming
   * EventVenue's fields, and inserts
   * the updated EventVenue back into the database.
   *
   * @param updatedEVenue the EventVenue to update the database instance with.
   * @return true if successfully updated, false otherwise.
   */
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
