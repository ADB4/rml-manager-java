package com.andybui.rmlmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "models_3d")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Model3D {

    @Id
    @Column(name = "item_id", nullable = false, unique = true)
    private String itemId;

    @NotBlank(message = "Item name is required")
    @Column(name = "item_name", nullable = false)
    private String itemName;

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
    private String lods;
}