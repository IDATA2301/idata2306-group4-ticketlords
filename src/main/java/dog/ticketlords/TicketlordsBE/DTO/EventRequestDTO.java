package dog.ticketlords.TicketlordsBE.DTO;

import java.time.LocalDate;

public record EventRequestDTO(
    String eventName,
    String host,
    long categoryId,
    LocalDate eventDateStart,
    LocalDate eventDateEnd,
    long venueId,
    String eventDescription,
    String imgPathUrl
) {}
