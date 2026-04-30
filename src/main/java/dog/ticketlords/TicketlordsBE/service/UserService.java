package dog.ticketlords.TicketlordsBE.service;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dog.ticketlords.TicketlordsBE.dbentity.RegisteredUser;
import dog.ticketlords.TicketlordsBE.dbentity.UnregisteredUser;
import dog.ticketlords.TicketlordsBE.dbentity.UserRole;
import dog.ticketlords.TicketlordsBE.repositories.RegisteredUserRepository;
import dog.ticketlords.TicketlordsBE.repositories.UnregisteredUserRepository;

/**
 * Service to handle operations directly with database concerning
 * {@link RegisteredUser}.
 * Includes methods like getting, inserting and removing users from the
 * database.
 */
@Service
@Transactional
public class UserService {

  private final RegisteredUserRepository regUserRepo;
  private final UnregisteredUserRepository unregUserRepo;
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  /**
   * Creates a new service for RegisteredUser operations.
   * 
   * @param regUserRepo   the repository to perform persistence operations for
   *                      {@link RegisteredUser}
   * @param unregUserRepo the repository to perform persistence operations for
   *                      {@link UnregisteredUser}
   */
  public UserService(RegisteredUserRepository regUserRepo, UnregisteredUserRepository unregUserRepo) {
    this.regUserRepo = regUserRepo;
    this.unregUserRepo = unregUserRepo;
  }

  /**
   * Returns an RegisteredUser from database.
   * 
   * @param userId the id of the user
   * @return an RegisteredUser from database as Optional
   */
  @Transactional(readOnly = true)
  public Optional<RegisteredUser> getRegUser(long userId) {
    return this.regUserRepo.findById(userId);
  }

  @Transactional(readOnly = true)
  public Optional<UnregisteredUser> getUnregUser(long userId) {
    return this.unregUserRepo.findById(userId);
  }

  /**
   * Creates and inserts a new UnregisteredUser into the database.
   * The user_id is generated automatically by the database.
   * 
   * @return the saved UnregisteredUser with generated id
   */
  public UnregisteredUser insertUnregisteredUserToDatabase() {
    return this.unregUserRepo.save(new UnregisteredUser());
  }

  /**
   * Checks if a user already exists.
   * 
   * @param user An object, of any user.
   * @return true, if any user exists, false otherwise.
   */
  public boolean userAlreadyExists(Object user) {
    if (user instanceof UnregisteredUser) {
      return this.unregUserRepo.exists(Example.of((UnregisteredUser) user));
    } else if (user instanceof RegisteredUser) {
      return this.regUserRepo.exists(Example.of((RegisteredUser) user));
    } else {
      return false;
    }
  }

  /**
   * Checks if {@link UnregisteredUser} exists, and if it does, encrypts the raw
   * password
   * of the user, before saving the {@link RegisteredUser} to the database.
   *
   * @param user the registered user to insert into the database
   * @throws IllegalArgumentException if the associated UnregisteredUser is not
   *                                  found
   */
  public boolean insertRegisteredUserToDatabase(RegisteredUser user, long unregId) {
    if (user == null || unregId < 0 || user.getEmail() == null
        || user.getHashedPassword() == null) {
      return false;
    }
    Optional<UnregisteredUser> unregUser = this.getUnregUser(unregId);
    if (unregUser.isEmpty()) { // checks database if unreg user exists
      return false;
    }
    RegisteredUser newUser = new RegisteredUser(
        unregUser.get(),
        user.getEmail(),
        user.getDisplayName(),
        user.getFirstName(),
        user.getLastName(),
        this.passwordEncoder.encode(user.getHashedPassword()),
        user.getPhoneNumber(),
        UserRole.USER);

    this.regUserRepo.save(newUser);
    return true;
  }

  /**
   * Checks if the hashed password in the database matches the raw password the
   * user tries to login with.
   *
   * @param rawPassword    the password the user types into the login field.
   * @param hashedPassword the hashed version of the user's password, stored in
   *                       the database.
   * @return
   */
  public boolean checkPassword(String rawPassword, String hashedPassword) {
    return passwordEncoder.matches(rawPassword, hashedPassword);
  }

  /**
   * Delete a user from the database.
   * 
   * @param userId the id of the user
   * @throws IllegalArgumentException if the associated {@link RegisteredUser} is
   *                                  not found
   */
  public boolean deleteRegisteredUserFromDatabase(long userId) {
    Optional<RegisteredUser> user = getRegUser(userId);
    if (user.isEmpty()) {
      return false;
    }
    this.regUserRepo.delete(user.get());
    return true;
  }

  /**
   * Updates an existing RegisteredUser in the database.
   * 
   * @param userId      the id of the user to update
   * @param updatedUser the updated user data
   * @return true if the user was successfully updated, false if user not found
   */
  public boolean updateRegisteredUser(long userId, RegisteredUser updatedUser) {
    Optional<RegisteredUser> existingUser = getRegUser(userId);
    if (existingUser.isEmpty()) {
      return false;
    }

    RegisteredUser user = existingUser.get();
    if (updatedUser.getEmail() != null) {
      user.setEmail(updatedUser.getEmail());
    }
    if (updatedUser.getHashedPassword() != null) {
      user.setHashedPassword(this.passwordEncoder.encode(updatedUser.getHashedPassword()));
    }
    if (updatedUser.getFirstName() != null) {
      user.setFirstName(updatedUser.getFirstName());
    }
    if (updatedUser.getLastName() != null) {
      user.setLastName(updatedUser.getLastName());
    }

    this.regUserRepo.save(user);
    return true;
  }

}
