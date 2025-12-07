package com.andybui.rmlmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetDto {

    @NotBlank(message = "Asset code is required")
    private String assetCode;

    @NotBlank(message = "Asset name is required")
    private String assetName;

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
    private List<Integer> lods = new ArrayList<>();
}