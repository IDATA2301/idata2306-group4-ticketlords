package dog.ticketlords.TicketlordsBE.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dog.ticketlords.TicketlordsBE.exception.ResourceNotFoundException;

/**
 * Global exception handler for the application.
 * Handles custom exceptions and provides consistent error responses across all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException and returns a 409 Conflict status.
     * 
     * @param ex the ResourceNotFoundException that was thrown
     * @return ResponseEntity with conflict status and error message
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
