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

  public Optional<BookingSite> getBookingSiteById(long id) {
    return this.bookingSiteRepository.findById(id);
  }

  /**
   * Inserts a booking site into the database, if it doesnt already exist.
   *
   * @param bs the booking site object to insert into the database.
   * @return true if the insertion succeeded, false otherwise.
   */
  public boolean insertBookingSiteToDatabase(BookingSite bs) {
    if (this.bookingSiteRepository.existsById(bs.getTicketVendorId())) {
      return false;
    } else {
      this.bookingSiteRepository.save(bs);
      return true;
    }
  }

  /**
   * Updates the current bookingsite's values in the datbase with the incoming
   * booking site's values, as long as the current booking site exists.
   *
   * @param updatedBookingSite The bookingSite to append values from, onto the
   *                           database's current bookingsite.
   * @return true if updated successfully, false otherwise.
   */
  public boolean updateBookingSite(BookingSite updatedBookingSite) {
    if (this.bookingSiteRepository.existsById(updatedBookingSite.getTicketVendorId())) {
      BookingSite databaseBookingSite = this.bookingSiteRepository.findById(updatedBookingSite.getTicketVendorId())
          .get();
      if (updatedBookingSite.getTicketVendor() != null) {
        databaseBookingSite.setTicketVendor(updatedBookingSite.getTicketVendor());
      }
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
  public boolean deleteBookingSite(long bookingSiteId) {
    if (this.bookingSiteRepository.findById(bookingSiteId).isPresent()) {
      this.bookingSiteRepository.deleteById(bookingSiteId);
      return true;
    }
    return false;
  }

  /**
   * Returns a list of all BookingSite objects with vendornames containing the non
   * case sensitive substring parameter.
   * If there are no matching objects, returns an empty list.
   *
   * @param bookingSiteNameSubstring the substring to match against BookingSite's
   *                                 vendorName field.
   * @return List of all bookingSite objects matching the substring, empty list of
   *         none matches.
   */
  public List<BookingSite> getBookingSitesByNameContaining(String bookingSiteNameSubstring) {
    return this.bookingSiteRepository.findAllByTicketVendorContainingIgnoreCase(bookingSiteNameSubstring);
  }

}
