package com.andybui.rmlmanager.dto;

import com.andybui.rmlmanager.model.GeometryFileType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeometryResponseDto {

    private String id;
    private String assetId;
    private String fileName;
    private String originalFileName;
    private GeometryFileType fileType;
    private String s3Key;
    private String s3Bucket;
    private Long fileSize;
    private String contentType;

    // Geometry Metadata
    private Integer vertexCount;
    private Integer polygonCount;
    private Integer triangleCount;
    private Boolean hasMaterials;
    private Boolean hasTextures;
    private Boolean hasAnimation;
    private Boolean hasSkeleton;

    // Bounding box
    private BoundingBox boundingBox;

    // Optional thumbnail
    private String thumbnailPath;

    // Version info
    private Double version;
    private Boolean isLatest;

    // Metadata
    private String checksum;
    private String uploadSource;
    private String uploadedBy;
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoundingBox {
        private Float minX;
        private Float minY;
        private Float minZ;
        private Float maxX;
        private Float maxY;
        private Float maxZ;
    }
}