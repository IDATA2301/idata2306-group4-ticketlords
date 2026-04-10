package dog.ticketlords.TicketlordsBE.DTO;

import java.math.BigDecimal;

public record VendorRatingDTO(String vendorName, BigDecimal avgVendorRating) {
}
