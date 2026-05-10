package dog.ticketlords.TicketlordsBE.utility;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
  
  @Value("${jwt.secret}")
  private String secretKey;
  
  @Value("${jwt.expiration:86400000}")
  private long expirationTime;
  
  public String generateToken(long userId) {
    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
    return Jwts.builder()
      .subject(String.valueOf(userId))
      .issuer("ticketlords")
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + expirationTime))
      .signWith(key, Jwts.SIG.HS512)
      .compact();
  }
}
