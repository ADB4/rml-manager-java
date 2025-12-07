package com.andybui.rmlmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "geometries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Geometry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false)
    private GeometryFileType fileType;
    @Column(name = "file_path", nullable = false)
    private String filePath;
    @Column(name = "file_size", nullable = false)
    private Long fileSize;
    @Column(name = "content_type")
    private String contentType;

    @Column(name = "s3_key", nullable = false)
    private String s3Key;

    @Column(name = "s3_bucket")
    private String s3Bucket;
    // metadata
    @Column(name = "vertex_count")
    private Integer vertexCount;
    @Column(name = "polygon_count")
    private Integer polygonCount;
    @Column(name = "triangle_count")
    private Integer triangleCount;
    @Column(name = "has_textures")
    private Boolean hasTextures;
    @Column(name = "has_animation")
    private Boolean hasAnimation;
    @Column(name = "has_skeleton")
    private Boolean hasSkeleton;

    // bounding box
    @Column(name = "bounds_min_x")
    private Float boundsMinX;
    @Column(name = "bounds_min_y")
    private Float boundsMinY;
    @Column(name = "bounds_min_z")
    private Float boundsMinZ;
    @Column(name = "bounds_max_x")
    private Float boundsMaxX;
    @Column(name = "bounds_max_y")
    private Float boundsMaxY;
    @Column(name = "bounds_max_z")
    private Float boundsMaxZ;

    @Column(name = "thumbnail_path")
    private String thumbnailPath;
    @Column(name = "version")
    private Double version;
    @Column(name = "is_latest")
    private Boolean isLatest;

    @Column(name = "checksum")
    private String checksum;
    @Column(name = "upload_source")
    private String uploadSource;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (version == null) {
            version = 0.0;
        }
        if (isLatest == null) {
            isLatest = true;
        }
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
