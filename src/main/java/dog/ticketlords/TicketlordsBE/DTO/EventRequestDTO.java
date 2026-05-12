package dog.ticketlords.TicketlordsBE.DTO;

import java.time.LocalDateTime;

public record EventRequestDTO(
    String eventName,
    String host,
    Long categoryId,
    LocalDateTime eventDateStart,
    LocalDateTime eventDateEnd,
    Long venueId,
    String eventDescription,
    String imgPathUrl) {
}
