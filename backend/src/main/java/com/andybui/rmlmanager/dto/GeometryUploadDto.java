package com.andybui.rmlmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeometryUploadDto {

    // Required fields
    private String assetId;

    // Optional metadata that can be provided during upload
    private Integer vertexCount;
    private Integer polygonCount;
    private Integer triangleCount;
    private Boolean hasMaterials;
    private Boolean hasTextures;
    private Boolean hasAnimation;
    private Boolean hasSkeleton;

    // Bounding box data
    private Float boundsMinX;
    private Float boundsMinY;
    private Float boundsMinZ;
    private Float boundsMaxX;
    private Float boundsMaxY;
    private Float boundsMaxZ;

    // Version info
    private Double version;

    // Notes about this geometry file
    private String notes;
}