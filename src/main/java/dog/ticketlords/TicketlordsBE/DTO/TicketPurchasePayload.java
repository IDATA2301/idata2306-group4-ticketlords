package dog.ticketlords.TicketlordsBE.DTO;

public record TicketPurchasePayload(
    long ticketId,
    int amount) {
}
