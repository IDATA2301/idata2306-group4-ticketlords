package dog.ticketlords.TicketlordsBE.service;

import dog.ticketlords.TicketlordsBE.dbentity.Category;
import dog.ticketlords.TicketlordsBE.dbentity.Ticket;
import dog.ticketlords.TicketlordsBE.repositories.TicketRepository;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

  private final TicketRepository ticketRepo;

  public TicketService(TicketRepository ticketRepo) {
    this.ticketRepo = ticketRepo;
  }

  /**
   * Gets a ticket by the ticket id.
   *
   * @param ticketId the id of the ticket.
   * @return the ticket whose id corresponds with the param ticketId.
   */
  public Optional<Ticket> getTicket(long ticketId) {
    return this.ticketRepo.findById(ticketId);
  }

  public List<Ticket> getTicketType(String ticketType) {
    return this.ticketRepo.findByTicketTypeContainingIgnoreCase(ticketType);
  }

  /**
   *
   * @param ticket
   * @return
   */
  public boolean insertTicketIntoDatabase(Ticket ticket) {
    if (!this.ticketRepo.existsById(ticket.getTicketId())) {
      this.ticketRepo.save(ticket);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Deletes a {@link Ticket} column in the database if it exists.
   *
   * @param ticketId the id of the ticket to delete.
   *
   * @return true if the deletion succeeded, false otherwise.
   */
  public boolean deleteTicket(long ticketId) {
    if (this.ticketRepo.existsById(ticketId)) {
      this.ticketRepo.deleteById(ticketId);
      return true;
    }
    return false;
  }


}
