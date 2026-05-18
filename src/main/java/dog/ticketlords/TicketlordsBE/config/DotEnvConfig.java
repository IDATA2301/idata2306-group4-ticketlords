package dog.ticketlords.TicketlordsBE.config;

import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Configuration class for loading environment variables from a .env file.
 * 
 * This class loads variables from the .env file located in the project root
 * and registers them as system properties. This allows Spring to resolve
 * property placeholders like ${JWT_SECRET} in application.properties.
 * 
 * The static block ensures the .env file is loaded before Spring processes
 * application.properties, guaranteeing that environment variables are available
 * to all configuration classes.
 */
@Configuration
public class DotEnvConfig {
  /**
   * Static initializer block that loads the .env file and registers
   * its variables as system properties.
   * 
   * Runs during class loading (before bean instantiation) to ensure
   * environment variables are available to Spring's property resolution.
   * If the .env file is missing, it is silently ignored.
   */
  static {
    Dotenv dotenv = Dotenv.configure()
        .filename(".env")
        .ignoreIfMissing()
        .load();

    dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
  }
}
