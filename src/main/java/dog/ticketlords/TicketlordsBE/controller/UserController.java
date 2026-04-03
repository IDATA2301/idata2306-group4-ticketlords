package dog.ticketlords.TicketlordsBE.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dog.ticketlords.TicketlordsBE.entity.RegisteredUser;
import dog.ticketlords.TicketlordsBE.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")

public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/user")
  public void insertOneUnregisteredUserIntoDatabase(@Valid @RequestBody RegisteredUser user) {
    this.userService.insertUnregisteredUserToDatabase();
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<RegisteredUser> getRegisteredUserById(@PathVariable int id) {
    Optional<RegisteredUser> user = this.userService.getRegUser(id);
    return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/user/{id}")
  public ResponseEntity<Void> deleteRegisteredUserById(@PathVariable int id) {
    if (this.userService.getRegUser(id).isPresent()) {
      this.userService.deleteRegisteredUserFromDatabase(id);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

}
