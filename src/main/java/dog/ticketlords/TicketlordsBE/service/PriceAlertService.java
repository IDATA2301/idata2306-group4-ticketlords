package dog.ticketlords.TicketlordsBE.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dog.ticketlords.TicketlordsBE.dbentity.PriceAlert;
import dog.ticketlords.TicketlordsBE.repositories.PriceAlertRepository;

@Service
public class PriceAlertService {

  private PriceAlertRepository priceAlertRepository;

  public PriceAlertService(PriceAlertRepository priceAlertRepository) {
    this.priceAlertRepository = priceAlertRepository;
  }

  public boolean createNewPriceAlert() {
    return true;
  }

  public List<PriceAlert> getAllPriceAlertsByUserId(long userId) {
    return this.priceAlertRepository.findAll();
  }

  /**
   * Updated the existing database entry (if it exists) with the values inside the provided {@link PriceAlert} object.
   *
   * @param updatedPriceAlert the PriceAlert object holding the new values to update the database entry with.
   * @return true if everything was successfull, false otherwise.
   */
  public boolean updateAlert(PriceAlert updatedPriceAlert) {
    if (this.priceAlertRepository.existsById(updatedPriceAlert.getAlertId())) {
      PriceAlert databaseAlert = this.priceAlertRepository.findById(updatedPriceAlert.getAlertId()).get();
      if (updatedPriceAlert.getIsActive() != null) {
        databaseAlert.setIsActive(updatedPriceAlert.getIsActive());
      }
      if (updatedPriceAlert.getTargetPrice() != null) {
        databaseAlert.setTargetPrice(updatedPriceAlert.getTargetPrice());
      }
      return true;
    } else {
      return false;
    }
  }
}
