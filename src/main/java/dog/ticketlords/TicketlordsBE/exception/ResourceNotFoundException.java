package dog.ticketlords.TicketlordsBE.exception;

/**
 * Exception thrown when a requested resource (Category, EventVenue, Event, etc.) is not found.
 * This exception maps to a 409 Conflict HTTP status code.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
