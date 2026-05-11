package dog.ticketlords.TicketlordsBE.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dog.ticketlords.TicketlordsBE.service.ImageStorageService;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

/**
 * Configuration for DigitalOcean Spaces (S3-compatible) client.
 *
 * <p>Creates and exposes an {@link S3Client} bean configured to communicate
 * with DigitalOcean Spaces using the AWS SDK v2. Credentials and endpoint
 * are injected from {@code application.properties} via environment variables,
 * keeping secrets out of source control.
 *
 * <p>Required properties:
 * <ul>
 *   <li>{@code spaces.endpoint} - Full Spaces endpoint URL (e.g. {@code https://lon1.digitaloceanspaces.com})</li>
 *   <li>{@code spaces.region}   - Region code (e.g. {@code lon1})</li>
 *   <li>{@code spaces.access-key} - Spaces access key ({@code ${SPACES_ACCESS_KEY}})</li>
 *   <li>{@code spaces.secret-key} - Spaces secret key ({@code ${SPACES_SECRET_KEY}})</li>
 * </ul>
 *
 * @see ImageStorageService
 */
@Configuration
public class SpacesConfig {

  /**
   * Constructs an {@link S3Client} pointed at DigitalOcean Spaces.
   *
   * @param endpoint  the Spaces endpoint URL
   * @param region    the Spaces region code
   * @param accessKey the Spaces access key
   * @param secretKey the Spaces secret key
   * @return a configured {@link S3Client} bean
   */
  @Bean
  S3Client s3Client(
            @Value("${spaces.endpoint}") String endpoint,
            @Value("${spaces.region}") String region,
            @Value("${spaces.access-key}") String accessKey,
            @Value("${spaces.secret-key}") String secretKey
    ) {
      return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }

}
