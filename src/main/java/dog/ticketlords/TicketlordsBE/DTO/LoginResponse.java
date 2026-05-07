package dog.ticketlords.TicketlordsBE.DTO;

/**
 * Response object for successful login requests.
 * Contains the JWT token that should be included in subsequent API requests.
 */
public class LoginResponse {
  private String token;
  
  /**
   * Constructs a LoginResponse with the provided JWT token.
   * 
   * @param token the JWT token to include in the response
   */
  public LoginResponse(String token) {
    this.token = token;
  }
  
  /**
   * Gets the JWT token.
   * 
   * @return the JWT token
   */
  public String getToken() {
    return token;
  }
  
  /**
   * Sets the JWT token.
   * 
   * @param token the JWT token to set
   */
  public void setToken(String token) {
    this.token = token;
  }
}
