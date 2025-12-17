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
@Table(name = "TextureSets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextureSet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Column(name = "s3_key", nullable = false)
    private String s3Key;

    @Column(name = "s3_bucket")
    private String s3Bucket;

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
