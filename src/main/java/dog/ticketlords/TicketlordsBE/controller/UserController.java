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
import dog.ticketlords.TicketlordsBE.dbentity.RegisteredUser;
import dog.ticketlords.TicketlordsBE.dbentity.UnregisteredUser;
import dog.ticketlords.TicketlordsBE.service.UserService;
import dog.ticketlords.TicketlordsBE.utility.JwtService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;
  private final JwtService jwtService;

  public UserController(UserService userService, JwtService jwtService) {
    this.userService = userService;
    this.jwtService = jwtService;
  }

  @PostMapping("/user/register")
  public ResponseEntity<Long> insertOneRegisteredUserIntoDatabase(@Valid @RequestBody RegisteredUser user, @RequestParam long uregId) {
    long newUserId = this.userService.insertRegisteredUserToDatabase(user, uregId);
    
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(newUserId);
  }

  @PostMapping("/user")
  public ResponseEntity<Long> insertUnregUserIntoDatabase() {
  UnregisteredUser user = this.userService.insertUnregisteredUserToDatabase();
  return ResponseEntity.status(HttpStatus.CREATED)
    .body(user.getUId());
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<RegisteredUser> getRegisteredUserById(@PathVariable long id) {
    Optional<RegisteredUser> user = this.userService.getRegUser(id);
    return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/user/{id}")
  public ResponseEntity<Void> deleteRegisteredUserById(@PathVariable long id) {
    if (this.userService.getRegUser(id).isPresent()) {
      this.userService.deleteRegisteredUserFromDatabase(id);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/unregistered/{id}")
  public ResponseEntity<UnregisteredUser> getUnregisteredUserById(@PathVariable long id){
    Optional<UnregisteredUser> unregisteredUser = this.userService.getUnregUser(id);
    return unregisteredUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

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

  @PutMapping("/user/{id}")
  public ResponseEntity<?> updateRegisteredUser(
      @PathVariable long id,
      @Valid @RequestBody RegisteredUser updatedUser) {
    if (this.userService.updateRegisteredUser(id, updatedUser)) {
      return ResponseEntity.ok(Map.of("success", true));
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}


