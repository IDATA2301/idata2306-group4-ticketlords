package dog.ticketlords.TicketlordsBE.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dog.ticketlords.TicketlordsBE.entity.RegisteredUser;
import dog.ticketlords.TicketlordsBE.entity.UnregisteredUser;
import dog.ticketlords.TicketlordsBE.repositories.UnregisteredUserRepository;
import dog.ticketlords.TicketlordsBE.repositories.RegisteredUserRepository;

/**
 * Service to handle operations directly with database concerning {@link RegisteredUser}.
 * Includes methods like getting, inserting and removing users from the database.
 */
@Service
@Transactional
public class UserService {

  private final RegisteredUserRepository regUserRepo;
  private final UnregisteredUserRepository unregUserRepo;

  /**
   * Creates a new service for RegisteredUser operations.
   * 
   * @param ruRepo the repository to perform persistence operations for {@link RegisteredUser}.
   */
  public UserService(RegisteredUserRepository regUserRepo, UnregisteredUserRepository unregUserRepo) {
    this.regUserRepo = regUserRepo;
    this.unregUserRepo = unregUserRepo;
  }

  /**
   * Returns an RegisteredUser from database.
   * @param userId the id of the user
   * @return an RegisteredUser from database as Optional
   */
  @Transactional(readOnly = true)
  public Optional<RegisteredUser> getRegUser(int userId) {
    return this.unregUserRepo.findById(userId).flatMap(this.regUserRepo::findById);
  }

  @Transactional(readOnly = true)
  public Optional<UnregisteredUser> getUnregUser(int userId) {
    return this.unregUserRepo.findById(userId);
  }

  /**
   * Creates and inserts a new UnregisteredUser into the database.
   * The user_id is generated automatically by the database.
   * @return the saved UnregisteredUser with generated id
   */
  public void insertUnregisteredUserToDatabase() {
    this.unregUserRepo.save(UnregisteredUser.create());
  }

  /**
   * Inserts a new registered user into the database.
   * Checks if {@link UnregisteredUser} does exists.
   * 
   * @param user the registered user to insert into the database
   * @throws IllegalArgumentException if the associated UnregisteredUser is not found
   */
  public void insertRegisteredUserToDatabase(RegisteredUser user) {
    if (this.getUnregUser(user.getUnregisteredUser().getUId()).isEmpty()) {
      throw new IllegalArgumentException("UnregisteredUser not found");
    }
    this.regUserRepo.save(user);
  }

  /**
   * Delete a user from the database.
   * 
   * @param userId the id of the user
   * @throws IllegalArgumentException if the associated {@link RegisteredUser} is not found
   */
  public void deleteRegisteredUserFromDatabase(int userId) {
    Optional<RegisteredUser> user = getRegUser(userId);
    if (user.isEmpty()) {
      throw new IllegalArgumentException("RegisteredUser not found");
    }
    this.regUserRepo.delete(user.get());
  }

}
