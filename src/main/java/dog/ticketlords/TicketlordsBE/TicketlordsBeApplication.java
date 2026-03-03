package dog.ticketlords.TicketlordsBE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dog.ticketlords.TicketlordsBE.DB.RegisteredUser;

@SpringBootApplication
public class TicketlordsBeApplication {

  public static void main(String[] args) {
    SpringApplication.run(TicketlordsBeApplication.class, args);
    RegisteredUser a = new RegisteredUser("l", "username", "firstName", "lastName", "hPassw", 123, "ADMIN");

  }

}
