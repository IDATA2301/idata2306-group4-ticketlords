package dog.ticketlords.TicketlordsBE.service;

import dog.ticketlords.TicketlordsBE.repositories.PriceAlertRepository;

public class PriceAlertService {

  private PriceAlertRepository priceAlertRepository;

  public PriceAlertService(PriceAlertRepository priceAlertRepository) {
    this.priceAlertRepository = priceAlertRepository;
  }

}
