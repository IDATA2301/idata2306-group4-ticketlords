package dog.ticketlords.TicketlordsBE.service;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dog.ticketlords.TicketlordsBE.DTO.UpdateUserDTO;
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
   * Returns an RegisteredUser from database using userId.
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
   * Returns an RegisteredUser from database using email.
   * 
   * @param userEmail the email of the user
   * @return an RegisteredUser from database as Optional
   */
  @Transactional(readOnly = true)
  public Optional<RegisteredUser> getRegUserByEmail(String email) {
    return this.regUserRepo.findByEmail(email);
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
  public long insertRegisteredUserToDatabase(RegisteredUser user, long unregId) {
    if (this.regUserRepo.existsById(unregId) || unregId < 0) {
      UnregisteredUser newUnregUser = this.insertUnregisteredUserToDatabase();
      unregId = newUnregUser.getUId();
    }

    Optional<UnregisteredUser> unregUser = this.getUnregUser(unregId);

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
    return unregId;
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
   * Checks whether the given user is an admin.
   * <p>
   * If the user does not exist, this returns {@code false}.
   *
   * @param userId the id of the {@link RegisteredUser} to check
   * @return {@code true} if the user exists and has role {@link UserRole#ADMIN},
   *         {@code false} otherwise
   */
  @Transactional(readOnly = true)
  public boolean isAdmin(long userId) {
    return regUserRepo.findById(userId).map(u -> u.getRole() == UserRole.ADMIN).orElse(false);
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
  public boolean updateRegisteredUser(long userId, UpdateUserDTO updatedUser) {
    Optional<RegisteredUser> existingUser = getRegUser(userId);
    if (existingUser.isEmpty()) {
      return false;
    }

    RegisteredUser user = existingUser.get();
    if (updatedUser.email() != null && !updatedUser.password().isEmpty()) {
      user.setEmail(updatedUser.email());
    }
    if (updatedUser.displayName() != null && !updatedUser.displayName().isEmpty()) {
      user.setDisplayName(updatedUser.displayName());
    }
    if (updatedUser.password() != null && !updatedUser.password().isEmpty()) {
      user.setHashedPassword(this.passwordEncoder.encode(updatedUser.password()));
    }
    if (updatedUser.firstName() != null && !updatedUser.firstName().isEmpty()) {
      user.setFirstName(updatedUser.firstName());
    }
    if (updatedUser.lastName() != null && !updatedUser.lastName().isEmpty()) {
      user.setLastName(updatedUser.lastName());
    }
    if (updatedUser.phoneNumber() != null) {
      user.setPhoneNumber(updatedUser.phoneNumber());
    }

    this.regUserRepo.save(user);
    return true;
  }

}
