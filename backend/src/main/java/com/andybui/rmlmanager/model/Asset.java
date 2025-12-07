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

@Entity
@Table(name = "assets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asset {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "asset_code", nullable = false, unique = true)
    private String assetCode;
    @NotBlank(message = "Item name is required")
    @Column(name = "asset_name", nullable = false)
    private String assetName;
    @NotBlank(message = "Category is required")
    @Column(name = "category", nullable = false)
    private String category;
    @NotBlank(message = "Subcategory is required")
    @Column(name = "subcategory", nullable = false)
    private String subcategory;
    @NotBlank(message = "Description is required")
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    @Column(name = "shader")
    private String shader;
    @NotBlank(message = "Material is required")
    @Column(name = "material", nullable = false)
    private String material;
    @NotNull(message = "Animation flag is required")
    @Column(name = "animation", nullable = false)
    private Boolean animation;
    @NotBlank(message = "LODs is required")
    @Column(name = "lods", nullable = false)
    private List<Integer> lods = new ArrayList<>();

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Geometry> geometries = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}