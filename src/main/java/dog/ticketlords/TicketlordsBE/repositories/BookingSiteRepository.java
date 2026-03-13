package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.entity.BookingSite;
import org.springframework.data.domain.Page;

public interface BookingSiteRepository extends JpaRepository<BookingSite, String> {

  // May be unnecessary, because apparently Sorubg Data Jpa generates some
  // implementations for us. Look into later.
  Page<BookingSite> getBookingSiteDescriptionByName(String vendorName);

}
