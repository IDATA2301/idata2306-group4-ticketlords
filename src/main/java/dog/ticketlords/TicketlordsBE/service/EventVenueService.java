package dog.ticketlords.TicketlordsBE.service;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import dog.ticketlords.TicketlordsBE.dbentity.EventVenue;
import dog.ticketlords.TicketlordsBE.repositories.EventVenueRepository;

@Service
public class EventVenueService {

  private final EventVenueRepository eventVenueRepo;

  public EventVenueService(EventVenueRepository eventVenueRepo) {
    this.eventVenueRepo = eventVenueRepo;
  }

  /**
   * Gets a list of event venues by any subset of address, arena, city or country.
   * It is ordered in said order.
   * 
   * @param address the address of the venue
   * @param arena the arena of the venue
   * @param city the city of the venue
   * @param country the country of the venue
   * @return a list of event venues sorted
   */
  public List<EventVenue> getEventVenueByAnySubset(String address, String arena, String city, String country) {

    boolean hasAddress = address != null && !address.isBlank();
    boolean hasArena   = arena   != null && !arena.isBlank();
    boolean hasCity    = city    != null && !city.isBlank();
    boolean hasCountry = country != null && !country.isBlank();

    if (!hasAddress && !hasArena && !hasCity && !hasCountry) {
        return List.of();
    }

    Set<EventVenue> results = new LinkedHashSet<>();

    if (hasAddress) results.addAll(eventVenueRepo.findByAddressIgnoreCase(address));
    if (hasArena)   results.addAll(eventVenueRepo.findByArenaIgnoreCase(arena));
    if (hasCity)    results.addAll(eventVenueRepo.findByCityIgnoreCase(city));
    if (hasCountry) results.addAll(eventVenueRepo.findByCountryIgnoreCase(country));

    return results.stream()
            .sorted(Comparator.comparing(EventVenue::getAddress)
                .thenComparing(EventVenue::getArena)
                .thenComparing(EventVenue::getCity)
                .thenComparing(EventVenue::getCountry))
            .toList();
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
   * Adds an {@link EventVenue} to the database, if the database doesnt already
   * contain an entry like
   * the one that is being added.
   *
   * @param an {@link EventVenue} object to insert to the database.
   * @return true if successfully added. False otherwise.
   */
  public boolean addEventVenueToDatabase(EventVenue eventVenue) {
    boolean exists = this.eventVenueRepo.findByAddressIgnoreCaseAndArenaIgnoreCaseAndCityIgnoreCaseAndCountryIgnoreCase(
        eventVenue.getAddress(), eventVenue.getArena(), eventVenue.getCity(), eventVenue.getCountry()).isPresent();
    if (!exists) {
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
  public List<EventVenue> getVenueByAddress(String venueAddress) {
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
  public boolean updateEventVenue(long venueId, EventVenue updatedEVenue) {
    if (this.eventVenueRepo.existsById(venueId)) {
      EventVenue databaseEVenue = this.eventVenueRepo.getReferenceById(venueId);
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
      this.eventVenueRepo.save(databaseEVenue);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Deletes an {@link EventVenue} if it exists in the database.
   *
   * @param id the id of the EventVenue to delete.
   * @return true if the deletion succeeded, false otherwise.
   */
  public boolean deleteEventVenueById(long id) {
    if (this.eventVenueRepo.existsById(id)) {
      this.eventVenueRepo.deleteById(id);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Counts the amount of {@link EventVenue} columns in the database.
   *
   * @return long representing the amount of EventVenue's in the database.
   */
  public long countEventVenues() {
    return this.eventVenueRepo.count();
  }

}
