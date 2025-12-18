package com.andybui.rmlmanager.service;

import com.andybui.rmlmanager.config.S3StorageProperties;
import com.andybui.rmlmanager.exception.FileStorageException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3StorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3StorageProperties s3Properties;
    private final ObjectMapper objectMapper;

    private S3Client createS3ClientForBucket(String region) {
        log.info("Creating S3 client for region: {}", region);

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                s3Properties.getAccessKeyId(),
                s3Properties.getSecretAccessKey()
        );

        S3Client client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        log.info("S3 client created for region: {}", region);
        return client;
    }

    public <T> T getJsonFromS3(String s3Key, Class<T> valueType) {
        return getJsonFromS3(s3Properties.getBucketName(), s3Key, s3Properties.getRegion(), valueType);
    }

    public <T> T getJsonFromS3(String bucketName, String s3Key, String region, Class<T> valueType) {
        S3Client client = null;
        boolean isExternalBucket = !bucketName.equals(s3Properties.getBucketName());

        try {
            // Use existing client for default bucket, create new one for external buckets
            client = isExternalBucket ? createS3ClientForBucket(region) : s3Client;

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> responseInputStream = client.getObject(getObjectRequest);
            T result = objectMapper.readValue(responseInputStream, valueType);

            log.info("Successfully retrieved and parsed JSON from S3: s3://{}/{}", bucketName, s3Key);
            return result;

        } catch (IOException ex) {
            log.error("Failed to read JSON from S3: s3://{}/{}", bucketName, s3Key, ex);
            throw new FileStorageException("Could not read JSON from S3: " + bucketName + "/" + s3Key, ex);
        } catch (Exception ex) {
            log.error("Failed to get JSON from S3: s3://{}/{}", bucketName, s3Key, ex);
            throw new FileStorageException("Could not get JSON from S3: " + bucketName + "/" + s3Key, ex);
        } finally {
            // Close external client but not the injected one
            if (isExternalBucket && client != null) {
                client.close();
            }
        }
    }


    public <T> List<T> getJsonArrayFromS3(String s3Key, Class<T> elementType) {
        return getJsonArrayFromS3(s3Properties.getBucketName(), s3Key, s3Properties.getRegion(), elementType);
    }

    public <T> List<T> getJsonArrayFromS3(String bucketName, String s3Key, String region, Class<T> elementType) {
        S3Client client = null;

        try {
            // Always create a new client with the specified region to avoid 301 errors
            client = createS3ClientForBucket(region);

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> responseInputStream = client.getObject(getObjectRequest);
            String jsonContent = new String(responseInputStream.readAllBytes());

            List<T> result = objectMapper.readValue(
                    jsonContent,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, elementType)
            );

            log.info("Successfully retrieved and parsed JSON array from S3: s3://{}/{} ({} items)",
                    bucketName, s3Key, result.size());
            return result;

        } catch (IOException ex) {
            log.error("Failed to read JSON array from S3: s3://{}/{}", bucketName, s3Key, ex);
            throw new FileStorageException("Could not read JSON array from S3: " + bucketName + "/" + s3Key, ex);
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    public String getTextFromS3(String s3Key) {
        return getTextFromS3(s3Properties.getBucketName(), s3Key, s3Properties.getRegion());
    }

    public String getTextFromS3(String bucketName, String s3Key, String region) {
        S3Client client = null;
        boolean isExternalBucket = !bucketName.equals(s3Properties.getBucketName());

        try {
            client = isExternalBucket ? createS3ClientForBucket(region) : s3Client;

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> responseInputStream = client.getObject(getObjectRequest);
            String content = new String(responseInputStream.readAllBytes());

            log.info("Successfully retrieved text from S3: s3://{}/{}", bucketName, s3Key);
            return content;

        } catch (IOException ex) {
            log.error("Failed to read text from S3: s3://{}/{}", bucketName, s3Key, ex);
            throw new FileStorageException("Could not read text from S3: " + bucketName + "/" + s3Key, ex);
        } finally {
            if (isExternalBucket && client != null) {
                client.close();
            }
        }
    }

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