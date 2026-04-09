package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.dbentity.BookingSite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingSiteRepository extends JpaRepository<BookingSite, Long> {

  // May be unnecessary, because apparently Sorubg Data Jpa generates some
  // implementations for us. Look into later.
  Page<BookingSite> findByTicketVendor(long vendorId, Pageable pageable);

}
