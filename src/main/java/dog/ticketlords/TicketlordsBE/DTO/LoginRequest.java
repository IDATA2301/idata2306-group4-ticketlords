package dog.ticketlords.TicketlordsBE.DTO;

public class LoginRequest {
  private String email;
  private String password;

  public LoginRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public LoginRequest() {
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }
}
