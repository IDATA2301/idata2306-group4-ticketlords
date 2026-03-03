package dog.ticketlords.TicketlordsBE.DB;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
@Entity
public class RegisteredUser {

  @EmbeddedId
  private CompositeKeyUser compositeKey;
  private String email;
  private String username;
  private String firstName;
  private String lastName;
  private String hashedPassword;
  private int phoneNumber;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  public RegisteredUser(String email, String username, String firstName, String lastName, String hPassw,
      int phoneNumber, UserRole role) {
    this.email = email;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.hashedPassword = hPassw;
    this.phoneNumber = phoneNumber;
  }
}
