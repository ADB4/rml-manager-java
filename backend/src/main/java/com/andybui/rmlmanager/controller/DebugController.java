package com.andybui.rmlmanager.controller;

import com.andybui.rmlmanager.config.S3StorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class DebugController {

    private final S3StorageProperties s3Properties;

    @GetMapping("/s3-test")
    public ResponseEntity<Map<String, Object>> testS3Access(
            @RequestParam String bucket,
            @RequestParam String key,
            @RequestParam String region) {

        Map<String, Object> response = new HashMap<>();

        try {
            log.info("Testing S3 access: s3://{}/{} in region {}", bucket, key, region);

            // Check credentials
            response.put("hasAccessKey", s3Properties.getAccessKeyId() != null && !s3Properties.getAccessKeyId().isEmpty());
            response.put("hasSecretKey", s3Properties.getSecretAccessKey() != null && !s3Properties.getSecretAccessKey().isEmpty());
            response.put("configuredBucket", s3Properties.getBucketName());
            response.put("configuredRegion", s3Properties.getRegion());

            // Try to create S3 client
            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                    s3Properties.getAccessKeyId(),
                    s3Properties.getSecretAccessKey()
            );

            S3Client s3Client = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                    .build();

            response.put("clientCreated", true);

            // Try to access the file
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            ResponseInputStream<GetObjectResponse> responseStream = s3Client.getObject(getObjectRequest);
            String content = new String(responseStream.readNBytes(500)); // Read first 500 bytes

            s3Client.close();

            response.put("success", true);
            response.put("contentPreview", content);
            response.put("contentLength", responseStream.response().contentLength());

        } catch (Exception ex) {
            log.error("S3 test failed", ex);
            response.put("success", false);
            response.put("error", ex.getClass().getSimpleName());
            response.put("message", ex.getMessage());
            response.put("stackTrace", getStackTraceString(ex));
        }

        return ResponseEntity.ok(response);
    }

    private String getStackTraceString(Exception ex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.toString()).append("\n");
        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
            if (sb.length() > 2000) break; // Limit size
        }
        return sb.toString();
    }
}