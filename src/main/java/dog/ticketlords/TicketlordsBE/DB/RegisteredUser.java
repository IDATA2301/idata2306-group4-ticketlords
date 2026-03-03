package dog.ticketlords.TicketlordsBE.DB;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
public class RegisteredUser {

  @EmbeddedId
  private CompositeKeyUser compositeKey;
  @Setter
  private String email;
  @Setter
  private String username;
  @Setter
  private String firstName;
  @Setter
  private String lastName;
  @Setter
  private String hashedPassword;
  @Setter
  private int phoneNumber;

  @Enumerated(EnumType.STRING)
  @Setter
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
