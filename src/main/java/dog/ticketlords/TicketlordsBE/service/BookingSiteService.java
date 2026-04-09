package dog.ticketlords.TicketlordsBE.service;

import java.util.List;
import java.util.Optional;

import dog.ticketlords.TicketlordsBE.dbentity.BookingSite;
import dog.ticketlords.TicketlordsBE.repositories.BookingSiteRepository;

public class BookingSiteService {

  private BookingSiteRepository bookingSiteRepository;

  public BookingSiteService(BookingSiteRepository bsRepo) {
    this.bookingSiteRepository = bsRepo;
  }

  public List<BookingSite> getAllBookingSites() {
    return this.bookingSiteRepository.findAll();
  }

  public Optional<BookingSite> getBookingSiteById(String name) {
    return this.bookingSiteRepository.findById(name);
  }

  /**
   * Inserts a booking site into the database, if it doesnt already exist.
   *
   * @param bs the booking site object to insert into the database.
   * @return true if the insertion succeeded, false otherwise.
   */
  public boolean insertBookingSiteToDatabase(BookingSite bs) {
    if (this.bookingSiteRepository.existsById(bs.getTicketVendor())) {
      return false;
    } else {
      this.bookingSiteRepository.save(bs);
      return true;
    }
  }

  // TODO: If the database changes, to not use ticketVendor's name as primary key,
  // need to update this method, so we can change the name of the name aswell.
  /**
   * Updates the current bookingsite's values in the datbase with the incoming
   * booking site's values, as long as the current booking site exists.
   *
   * @param updatedBookingSite The bookingSite to append values from, onto the
   *                           database's current bookingsite.
   * @return true if updated successfully, false otherwise.
   */
  public boolean updateBookingSite(BookingSite updatedBookingSite) {
    if (this.bookingSiteRepository.existsById(updatedBookingSite.getTicketVendor())) {
      BookingSite databaseBookingSite = this.bookingSiteRepository.findById(updatedBookingSite.getTicketVendor()).get();
      if (updatedBookingSite.getBookingSiteDescription() != null) {
        databaseBookingSite.setBookingSiteDescription(updatedBookingSite.getBookingSiteDescription());
      }
      if (updatedBookingSite.getWebsiteLink() != null) {
        databaseBookingSite.setWebsiteLink(updatedBookingSite.getBookingSiteDescription());
      }
      return true;
    }
    return false;
  }

  /**
   * Deletes the booking site entry in the database if it exists.
   *
   * @param bookingSiteName the name of the bookingSite to delete the entry of.
   * @return true if the deletion is successfull, false otherwise.
   */
  public boolean deleteBookingSite(String bookingSiteName) {
    if (this.bookingSiteRepository.findById(bookingSiteName).isPresent()) {
      this.bookingSiteRepository.deleteById(bookingSiteName);
      return true;
    }
    return false;
  }

}
