package dog.ticketlords.TicketlordsBE.DTO;

import java.time.LocalDateTime;

public record EventRequestDTO(
    String eventName,
    String host,
    long categoryId,
    LocalDateTime eventDateStart,
    LocalDateTime eventDateEnd,
    long venueId,
    String eventDescription,
    String imgPathUrl) {
}
