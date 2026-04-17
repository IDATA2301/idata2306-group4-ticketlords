package dog.ticketlords.TicketlordsBE.controller;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dog.ticketlords.TicketlordsBE.dbentity.RegisteredUser;
import dog.ticketlords.TicketlordsBE.dbentity.UnregisteredUser;
import dog.ticketlords.TicketlordsBE.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")

public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/user/register")
  public ResponseEntity<Void> insertOneRegisteredUserIntoDatabase(@Valid @RequestBody RegisteredUser user) {
    if (this.userService.insertRegisteredUserToDatabase(user)) {
      return ResponseEntity.created(URI.create("/users/user/" + user.getUserId())).build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/user")
  public ResponseEntity<?> insertUnregUserIntoDatabase() {
    UnregisteredUser user = this.userService.insertUnregisteredUserToDatabase();
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(Map.of("unregisteredUserId", user.getUId()));
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
  }

}
