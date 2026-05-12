package dog.ticketlords.TicketlordsBE.DTO;

import java.util.List;

import dog.ticketlords.TicketlordsBE.dbentity.Event;

public record RecommendedEventsDTO(List<Event> recommendedEvents) {
}
