package com.andybui.rmlmanager.service;

import com.andybui.rmlmanager.config.S3StorageProperties;
import com.andybui.rmlmanager.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3StorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3StorageProperties s3Properties;

    /**
     * Upload file to S3 and return the S3 key
     */

    /*
    NEVER commit secrets. store as env variables:
    export AWS_ACCESS_KEY_ID=your_staging_access_key
    export AWS_SECRET_ACCESS_KEY=your_staging_secret_key
    export AWS_REGION=us-east-1
    getting a json from s3
    private final S3Client s3Client;
    private final ObjectMapper objectMapper;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public S3Service(S3Client s3Client, ObjectMapper objectMapper) {
        this.s3Client = s3Client;
        this.objectMapper = objectMapper;
    }

    public <T> T getJsonFromS3(String key, Class<T> valueType) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            ResponseInputStream<?> response = s3Client.getObject(getObjectRequest);
            return objectMapper.readValue(response, valueType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON from S3", e);
        }
    }
    */

    public String uploadFile(MultipartFile file, String assetId) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Validate filename
            if (originalFileName.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence: " + originalFileName);
            }

            // Generate unique S3 key
            String fileExtension = getFileExtension(originalFileName);
            String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
            String s3Key = s3Properties.getGeometriesPrefix() + assetId + "/" + uniqueFileName;

            // Upload to S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .metadata(java.util.Map.of(
                            "original-filename", originalFileName,
                            "asset-id", assetId
                    ))
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            log.info("Uploaded file to S3: {} (bucket: {})", s3Key, s3Properties.getBucketName());
            return s3Key;

        } catch (IOException ex) {
            throw new FileStorageException("Could not upload file to S3: " + originalFileName, ex);
        }
    }

    /**
     * Generate presigned URL for downloading a file
     */
    public String generatePresignedUrl(String s3Key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(s3Key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(s3Properties.getPresignedUrlExpirationMinutes()))
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            String url = presignedRequest.url().toString();

            log.debug("Generated presigned URL for: {}", s3Key);
            return url;

        } catch (Exception ex) {
            throw new FileStorageException("Could not generate presigned URL for: " + s3Key, ex);
        }
    }

    /**
     * Delete file from S3
     */
    public void deleteFile(String s3Key) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(s3Key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("Deleted file from S3: {}", s3Key);

        } catch (Exception ex) {
            log.error("Could not delete file from S3: {}", s3Key, ex);
            throw new FileStorageException("Could not delete file from S3: " + s3Key, ex);
        }
    }

    /**
     * Check if file exists in S3
     */
    public boolean fileExists(String s3Key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(s3Key)
                    .build();

            s3Client.headObject(headObjectRequest);
            return true;

        } catch (NoSuchKeyException ex) {
            return false;
        }
    }

    /**
     * Get file metadata from S3
     */
    public HeadObjectResponse getFileMetadata(String s3Key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(s3Key)
                    .build();

            return s3Client.headObject(headObjectRequest);

        } catch (Exception ex) {
            throw new FileStorageException("Could not get file metadata from S3: " + s3Key, ex);
        }
    }

    /**
     * Copy file within S3 (useful for versioning)
     */
    public String copyFile(String sourceKey, String destinationKey) {
        try {
            CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                    .sourceBucket(s3Properties.getBucketName())
                    .sourceKey(sourceKey)
                    .destinationBucket(s3Properties.getBucketName())
                    .destinationKey(destinationKey)
                    .build();

            s3Client.copyObject(copyObjectRequest);
            log.info("Copied file in S3 from {} to {}", sourceKey, destinationKey);
            return destinationKey;

        } catch (Exception ex) {
            throw new FileStorageException("Could not copy file in S3", ex);
        }
    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return fileName.substring(lastIndexOf + 1);
    }
}