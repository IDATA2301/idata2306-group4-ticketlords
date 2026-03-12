package dog.ticketlords.TicketlordsBE.DB;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representation of the database registered_user table.
 * Reoresents a registered user with personal details, credentials,
 * and a role.
 * Each RegisteredUser is linked to an {@link UnregisteredUser} via
 * a one-to-one relationship.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "registered_user")
public class RegisteredUser {

  @Id
  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
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
  @Column(name = "user_role", columnDefinition = "role_type")
  private UserRole role;

  /**
   * Constructs a RegisteredUser. Should only be used for testing.
   *
   * @param unregisteredUser the associated {@link UnregisteredUser} entity.
   * @param email            the email address of the user.
   * @param username         the username of the user.
   * @param firstName        the first name of the user.
   * @param lastName         the last name of the user.
   * @param hPassw           the hashed password of the user.
   * @param phoneNumber      the phone number of the user.
   * @param role             the {@link UserRole} assigned to the user.
   */
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
