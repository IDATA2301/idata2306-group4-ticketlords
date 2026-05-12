package dog.ticketlords.TicketlordsBE.DTO;

/**
 * Record holding all values needed to update a registered user's details.
 */
public record UpdateUserDTO(
    String email,
    String displayName,
    String firstName,
    String lastName,
    String password,
    Integer phoneNumber) {
}
