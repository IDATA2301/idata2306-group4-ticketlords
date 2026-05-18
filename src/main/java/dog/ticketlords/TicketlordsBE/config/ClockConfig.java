package dog.ticketlords.TicketlordsBE.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@SuppressWarnings("unused")
public class ClockConfig {

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }
}
