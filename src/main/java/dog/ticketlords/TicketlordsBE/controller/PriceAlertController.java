package dog.ticketlords.TicketlordsBE.controller;

import dog.ticketlords.TicketlordsBE.dbentity.PriceAlert;
import dog.ticketlords.TicketlordsBE.service.PriceAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("")
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

//TODO fix mapping link
  
  @GetMapping("/")
  public ResponseEntity<List<PriceAlert>> getAllPriceAlertsByUserId(@PathVariable long userId) {
    return ResponseEntity.ok(this.priceAlertService.getAllPriceAlertsByUserId(userId));
  }

}
