package dog.ticketlords.TicketlordsBE.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


/**
 * Service for uploading images to DigitalOcean Spaces (S3-compatible storage).
 * <p>
 * Uploaded files are stored under the {@code images/} prefix with a UUID to avoid
 * name collisions. Returns a publicly accessible URL to the uploaded file.
 * </p>
 */
@Service
public class ImageStorageService {

  private final S3Client s3;
  private final String bucket;
  private final String endpoint;

  /**
   * Constructs the service with the required S3 client and storage configuration.
   *
   * @param s3       the AWS S3-compatible client configured for DigitalOcean Spaces
   * @param bucket   the name of the Spaces bucket, injected from {@code spaces.bucket}
   * @param endpoint the base endpoint URL, injected from {@code spaces.endpoint}
   *                 (e.g. {@code https://lon1.digitaloceanspaces.com/})
   */
  public ImageStorageService(S3Client s3, 
                           @Value("${spaces.bucket}") String bucket,
                           @Value("${spaces.endpoint}") String endpoint) {
    this.s3 = s3;
    this.bucket = bucket;
    this.endpoint = endpoint;
}

  /**
   * Uploads an image file to DigitalOcean Spaces and returns its public URL.
   * <p>
   * The file is stored at {@code images/<uuid>-<originalFilename>} within the
   * configured bucket. The returned URL is constructed from the endpoint, bucket,
   * and key, and can be used directly in an {@code <img>} tag on the frontend.
   * </p>
   *
   * @param file the image file to upload
   * @return the full public URL of the uploaded image
   * @throws IOException if reading the file bytes fails
   */
  public String upload(MultipartFile file) throws IOException {
    String key = "images/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

    byte[] bytes = file.getBytes();

    s3.putObject(
            PutObjectRequest.builder()
                      .bucket(bucket)
                      .key(key)
                      .contentType(file.getContentType())
                      .build(),
            RequestBody.fromBytes(bytes)
    );
    return this.endpoint + this.bucket + "/" + key;
  }
}
