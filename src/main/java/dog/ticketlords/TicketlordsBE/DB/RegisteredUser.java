package dog.ticketlords.TicketlordsBE.DB;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "registered_user")
public class RegisteredUser {

  @Id
  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  @Column(name = "user_id")
  private UnregisteredUser unregisteredUser;
  @Setter
  @Column(name = "email", nullable = false)
  private String email;
  @Setter
  @Column(name = "username")
  private String username;
  @Setter
  @Column(name = "first_name")
  private String firstName;
  @Setter
  @Column(name = "last_name")
  private String lastName;
  @Setter
  @Column(name = "password", nullable = false, unique = true)
  private String hashedPassword;
  @Setter
  @Column(name = "phonenumber")
  private int phoneNumber;

  @Enumerated(EnumType.STRING)
  @Setter
  // TODO:Need table for this in the database.
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
