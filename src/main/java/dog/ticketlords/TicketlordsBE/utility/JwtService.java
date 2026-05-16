package dog.ticketlords.TicketlordsBE.utility;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
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

  /**
   * Extracts the user id stored in the JWT "sub" (subject) field.
   *
   * @param token a raw JWT token (no "Bearer " prefix)
   * @return user id, or {@code null} if missing/invalid
   */
  public Long extractUserId(String token) {
    if (token == null || token.isBlank()) {
      return null;
    }

    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
    Claims claims = Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();

    String subject = claims.getSubject();
    if (subject == null || subject.isBlank()) {
      return null;
    }

    return Long.valueOf(subject);
  }
}
