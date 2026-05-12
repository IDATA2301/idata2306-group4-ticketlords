package dog.ticketlords.TicketlordsBE.service;

import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class ImageStorageService {

  private final S3Client s3;
  private final String bucket;

  public ImageStorageService(S3Client s3, @Value("${spaces.bucket}") String bucket) {
    this.s3 = s3;
    this.bucket = bucket;
  }

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
}
