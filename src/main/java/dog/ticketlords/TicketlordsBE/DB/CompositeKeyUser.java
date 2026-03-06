package dog.ticketlords.TicketlordsBE.DB;

import java.io.Serializable;

import com.fasterxml.jackson.databind.type.PlaceholderForType;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

//TODO: consider delete this class
@Embeddable
public class CompositeKeyUser implements Serializable {
  @Getter
  private int uID;
  @Getter
  private String uName;

  public CompositeKeyUser(int uID, String uName) {
    this.uID = uID;
    this.uName = uName;
  }

}
