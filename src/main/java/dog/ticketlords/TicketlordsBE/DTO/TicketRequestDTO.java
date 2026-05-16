package dog.ticketlords.TicketlordsBE.DTO;

import java.math.BigDecimal;

public record TicketRequestDTO (
  Long eventId,
  String ticketType,
  BigDecimal price,
  Integer amountAvailable,
  String ticketDescription) {
}
