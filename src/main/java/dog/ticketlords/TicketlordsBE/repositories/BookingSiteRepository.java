package dog.ticketlords.TicketlordsBE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.ticketlords.TicketlordsBE.dbentity.BookingSite;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingSiteRepository extends JpaRepository<BookingSite, Long> {

  Page<BookingSite> findByTicketVendor(long vendorId, Pageable pageable);

  List<BookingSite> findAllByTicketVendorContainingIgnoreCase(String nameSubstring);

  Optional<BookingSite> findByTicketVendor(String fullVendorName);

}
