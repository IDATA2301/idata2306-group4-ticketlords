package dog.ticketlords.TicketlordsBE.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dog.ticketlords.TicketlordsBE.DTO.LoginRequest;
import dog.ticketlords.TicketlordsBE.DTO.LoginResponse;
import dog.ticketlords.TicketlordsBE.DTO.UpdateUserDTO;
import dog.ticketlords.TicketlordsBE.dbentity.RegisteredUser;
import dog.ticketlords.TicketlordsBE.dbentity.UnregisteredUser;
import dog.ticketlords.TicketlordsBE.service.UserService;
import dog.ticketlords.TicketlordsBE.utility.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * The UserController class is a REST controller that handles HTTP requests
 * related
 * to user management, including registration, login, retrieval, update, and
 * deletion of user accounts.
 */
@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "APIs for managing registered and unregistered users, including registration, login, retrieval, update, and deletion of user accounts.")
public class UserController {

  private final UserService userService;
  private final JwtService jwtService;

  public UserController(UserService userService, JwtService jwtService) {
    this.userService = userService;
    this.jwtService = jwtService;
  }

  /**
   * Registers a new user by inserting their details into the database. The
   * registered user's id
   * is set based on the unregisteredUser id.
   *
   * @param user   the RegisteredUser object containing the user's details to be
   *               registered
   * @param uregId the id of the unregistered user to be associated with the new
   *               registered user
   * @return ResponseEntity containing the id of the newly registered user and a
   *         status of 201 if successful.
   */
  @Operation(summary = "Register a new user", description = "Registers a new user by inserting their details into the database. The registered user's id is set based on the unregisteredUser id.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "User registered successfully, returns the id of the newly registered user."),
      @ApiResponse(responseCode = "400", description = "Bad Request - invalid user details or unregistered user ID.")
  })
  @PostMapping("/user/register")
  public ResponseEntity<Long> insertOneRegisteredUserIntoDatabase(@Valid @RequestBody RegisteredUser user,
      @RequestParam long uregId) {
    try {
      long newUserId = this.userService.insertRegisteredUserToDatabase(user, uregId);

      return ResponseEntity.status(HttpStatus.CREATED)
          .body(newUserId);

    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  /**
   * Inserts an {@link UnregisteredUser} into the database.
   */
  @PostMapping("/user")
  @Operation(summary = "Insert an unregistered user into the database", description = "Inserts an unregistered user into the database and returns the id of the newly created unregistered user.")
  @ApiResponse(responseCode = "201", description = "Unregistered user inserted successfully, returns the id of the newly created unregistered user.")
  public ResponseEntity<Long> insertUnregUserIntoDatabase() {
    UnregisteredUser user = this.userService.insertUnregisteredUserToDatabase();
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(user.getUId());
  }

  /**
   * Gets the registered user with the specified id.
   *
   * @param id the id of the registered user to retrieve
   * @return ResponseEntity containing the registered user if found, or a 404
   *         status if not found.
   */
  @Operation(summary = "Get registered user by ID", description = "Retrieves a registered user from the database based on the provided ID.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Registered user found and returned successfully."),
      @ApiResponse(responseCode = "404", description = "Registered user with the specified ID not found in the database.")
  })
  @GetMapping("/user/{id}")
  public ResponseEntity<RegisteredUser> getRegisteredUserById(@PathVariable long id) {
    Optional<RegisteredUser> user = this.userService.getRegUser(id);
    return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/user/{id}/is-admin")
  public ResponseEntity<Map<String, Boolean>> isAdmin(@PathVariable long id) {
    boolean isAdmin = userService.isAdmin(id);
    return ResponseEntity.ok(Map.of("isAdmin", isAdmin));
  }

  /**
   * Deletes the registered user with the specified id.
   *
   * @param id the id of the registered user to delete
   * @return ResponseEntity with a 204 status if the user was deleted, or a 404
   *         status if the user was not found.
   */
  @Operation(summary = "Delete registered user by ID", description = "Deletes a registered user from the database based on the provided ID.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Registered user deleted successfully."),
      @ApiResponse(responseCode = "404", description = "Registered user with the specified ID not found in the database.")
  })
  @DeleteMapping("/user/{id}")
  public ResponseEntity<Void> deleteRegisteredUserById(@PathVariable long id) {
    if (this.userService.getRegUser(id).isPresent()) {
      this.userService.deleteRegisteredUserFromDatabase(id);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Gets the unregistered user with the specified id.
   *
   * @param id the id of the unregistered user to retrieve
   * @return ResponseEntity containing the unregistered user if found, or a 404
   */
  @Operation(summary = "Get unregistered user by ID", description = "Retrieves an unregistered user from the database based on the provided ID.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Unregistered user found and returned successfully."),
      @ApiResponse(responseCode = "404", description = "Unregistered user with the specified ID not found in the database.")
  })
  @GetMapping("/unregistered/{id}")
  public ResponseEntity<UnregisteredUser> getUnregisteredUserById(@PathVariable long id) {
    Optional<UnregisteredUser> unregisteredUser = this.userService.getUnregUser(id);
    return unregisteredUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Authenticates a user based on their email and password. If the credentials
   * are valid, a JWT token is generated and returned in the response.
   *
   * @param request the LoginRequest object containing the user's email and
   *                password for authentication.
   * @return ResponseEntity containing a LoginResponse with the generated JWT
   *         token if authentication is successful, or a 401 status if
   *         authentication fails.
   */
  @Operation(summary = "User login", description = "Authenticates a user based on their email and password. If the credentials are valid, a JWT token is generated and returned in the response.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User authenticated successfully, returns a JWT token."),
      @ApiResponse(responseCode = "401", description = "Unauthorized - invalid email or password.")
  })
  @PostMapping("/user/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    Optional<RegisteredUser> actualUser = this.userService.getRegUserByEmail(request.getEmail());
    if (actualUser.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    boolean isValid = userService.checkPassword(request.getPassword(), actualUser.get().getHashedPassword());

    if (isValid) {
      String token = jwtService.generateToken(actualUser.get().getUserId());
      return ResponseEntity.ok(new LoginResponse(token));
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  /**
   * Updates an already registerd user with the credentials in the body of the
   * request.
   *
   * @param id          the id of the registered user to update
   * @param updatedUser the UpdateUserDTO object containing the updated user
   *                    details
   * @return ResponseEntity containing the updated user details if the update was
   *         successful, or a 404 status if the user was not found.
   */
  @Operation(summary = "Update registered user", description = "Updates an already registered user with the credentials in the body of the request.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Registered user updated successfully, returns the updated user details."),
      @ApiResponse(responseCode = "404", description = "Registered user with the specified ID not found in the database.")
  })
  @PutMapping("/user/{id}")
  public ResponseEntity<UpdateUserDTO> updateRegisteredUser(
      @PathVariable long id,
      @RequestBody UpdateUserDTO updatedUser) {
    if (this.userService.updateRegisteredUser(id, updatedUser)) {
      return ResponseEntity.ok(updatedUser);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
