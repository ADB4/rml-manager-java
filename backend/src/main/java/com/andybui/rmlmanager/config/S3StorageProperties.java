package com.andybui.rmlmanager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws.s3")
@Data
public class FileStorageProperties {
    private String bucketName;
    private String region;
    private String accessKeyId;
    private String secretAccessKey;
    private String geometriesPrefix = "geometries/";
    private String thumbnailsPrefix = "thumbnails/";
    private long maxFileSize = 524288000;
    private int presignedUrlExpirationMinutes = 60;
}
