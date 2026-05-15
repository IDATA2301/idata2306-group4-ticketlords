package dog.ticketlords.TicketlordsBE.controller;

import dog.ticketlords.TicketlordsBE.dbentity.PriceAlert;
import dog.ticketlords.TicketlordsBE.service.PriceAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("")
@Tag(name = "Price Alert Controller", description = "!!! Deprecated !!!")
public class PriceAlertController {
  private final PriceAlertService priceAlertService;

  /**
   * Constructs a PriceAlertController with the provided PriceAlertService.
   *
   * @param priceAlertService the PriceAlert service to be used
   */
  public PriceAlertController(PriceAlertService priceAlertService) {
    this.priceAlertService = priceAlertService;
  }

  @Operation(summary = "Get all price alerts for a user", description = "Returns a list of price alerts for the specified user ID.", deprecated = true)
  @GetMapping("/")
  public ResponseEntity<List<PriceAlert>> getAllPriceAlertsByUserId(@PathVariable long userId) {
    return ResponseEntity.ok(this.priceAlertService.getAllPriceAlertsByUserId(userId));
  }

}
