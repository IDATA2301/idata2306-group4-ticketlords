package dog.ticketlords.TicketlordsBE.service;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;


/**
 * Service for uploading images to DigitalOcean Spaces (S3-compatible storage) and
 * generating short-lived access URLs.
 *
 * <p>Images are stored under the {@code images/} prefix with a UUID to avoid name
 * collisions. For private buckets, callers should store the returned object key in
 * the database and request a fresh presigned URL whenever the frontend needs to
 * load the image.</p>
 */
@Service
public class ImageStorageService {

  private final S3Client s3;
  private final S3Presigner presigner;
  private final String bucket;

  /**
   * Constructs the service with the required S3 client and storage configuration.
   *
    * @param s3 the S3-compatible client configured for DigitalOcean Spaces
    * @param presigner the presigner used to mint short-lived access URLs
    * @param bucket the name of the Spaces bucket, injected from {@code spaces.bucket}
   */
  public ImageStorageService(
                          S3Client s3, 
                          S3Presigner presigner,
                @Value("${spaces.bucket}") String bucket) {
    this.s3 = s3;
    this.presigner = presigner;
    this.bucket = bucket;
}

  /**
   * Uploads an image file to DigitalOcean Spaces and returns its object key.
    *
    * <p>The file is stored at {@code images/<uuid>-<originalFilename>} within the
    * configured bucket. The returned key is stable and should be stored in the
    * database; to display the image later, generate a presigned URL using
    * {@link #getPresignedUrl(String)}.</p>
   *
   * @param file the image file to upload
   * @return the object key of the uploaded image (store this in the database)
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
    return key;
  }

  /**
   * Generates a short-lived presigned URL for downloading an object from DigitalOcean
   * Spaces.
   *
   * <p>This is intended for private buckets/objects. The {@code objectKey} is the path
   * within the bucket (for example {@code images/<uuid>-<filename>}), not a full URL.</p>
   *
   * <p>The returned URL expires after 10 minutes. After it expires, clients must request
   * a new URL.</p>
   *
   * @param objectKey the object key inside the configured bucket
   * @return a presigned URL that can be used to perform an HTTP GET until it expires
   */
  public String getPresignedUrl(String objectKey) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(objectKey)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(10))
        .getObjectRequest(getObjectRequest)
        .build();

    PresignedGetObjectRequest presigned = presigner.presignGetObject(presignRequest);
    return presigned.url().toString();
  }
}
