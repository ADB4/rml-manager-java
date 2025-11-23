package com.andybui.rmlmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Model3DRequest {

    @NotBlank(message = "Item ID is required")
    private String itemId;

    @NotBlank(message = "Item name is required")
    private String itemName;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Subcategory is required")
    private String subcategory;

    @NotBlank(message = "Description is required")
    private String description;

    private String notes;

    private String shader;

    @NotBlank(message = "Material is required")
    private String material;

    @NotNull(message = "Animation flag is required")
    private Boolean animation;

    @NotBlank(message = "LODs is required")
    private String lods;
}