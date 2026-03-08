package dog.ticketlords.TicketlordsBE.DB;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
public class RegisteredUser {

  @Id
  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private UnregisteredUser unregisteredUser;
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

  public RegisteredUser(UnregisteredUser unregisteredUser, String email, String username, String firstName,
      String lastName, String hPassw,
      int phoneNumber, UserRole role) {
    this.unregisteredUser = unregisteredUser;
    this.email = email;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.hashedPassword = hPassw;
    this.phoneNumber = phoneNumber;
    this.role = role;
  }
}
