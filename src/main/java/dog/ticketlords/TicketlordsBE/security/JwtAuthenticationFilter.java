package dog.ticketlords.TicketlordsBE.security;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import dog.ticketlords.TicketlordsBE.repositories.RegisteredUserRepository;
import dog.ticketlords.TicketlordsBE.utility.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final RegisteredUserRepository registeredUserRepository;

  public JwtAuthenticationFilter(JwtService jwtService, RegisteredUserRepository registeredUserRepository) {
    this.jwtService = jwtService;
    this.registeredUserRepository = registeredUserRepository;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authorizationHeader.substring("Bearer ".length()).trim();

    try {
      Long userId = jwtService.extractUserId(token);
      if (userId == null) {
        filterChain.doFilter(request, response);
        return;
      }

      registeredUserRepository.findById(userId).ifPresent(user -> {
        String role = user.getRole() == null ? null : user.getRole().name();
        List<SimpleGrantedAuthority> authorities = role == null
            ? List.of()
            : List.of(new SimpleGrantedAuthority("ROLE_" + role));

        var authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      });

      filterChain.doFilter(request, response);
    } catch (RuntimeException ex) {
      // Invalid / expired token => leave unauthenticated.
      filterChain.doFilter(request, response);
    }
  }
}
