package com.andybui.rmlmanager.mapper;

import com.andybui.rmlmanager.dto.GeometryResponseDto;
import com.andybui.rmlmanager.model.Geometry;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GeometryMapper {

    public GeometryResponseDto toResponse(Geometry geometry) {
        if (geometry == null) {
            return null;
        }

        GeometryResponseDto response = new GeometryResponseDto();
        response.setId(String.valueOf(geometry.getId()));
        response.setAssetId(geometry.getAsset() != null ? geometry.getAsset().getId() : null);
        response.setFileName(geometry.getFileName());
        response.setFileType(geometry.getFileType());
        response.setS3Key(geometry.getS3Key());
        response.setS3Bucket(geometry.getS3Bucket());
        response.setFileSize(geometry.getFileSize());
        response.setContentType(geometry.getContentType());

        // Metadata
        response.setVertexCount(geometry.getVertexCount());
        response.setPolygonCount(geometry.getPolygonCount());
        response.setTriangleCount(geometry.getTriangleCount());
        response.setHasTextures(geometry.getHasTextures());
        response.setHasAnimation(geometry.getHasAnimation());
        response.setHasSkeleton(geometry.getHasSkeleton());

        // Bounding box
        if (hasAnyBoundingBoxData(geometry)) {
            GeometryResponseDto.BoundingBox bbox = new GeometryResponseDto.BoundingBox();
            bbox.setMinX(geometry.getBoundsMinX());
            bbox.setMinY(geometry.getBoundsMinY());
            bbox.setMinZ(geometry.getBoundsMinZ());
            bbox.setMaxX(geometry.getBoundsMaxX());
            bbox.setMaxY(geometry.getBoundsMaxY());
            bbox.setMaxZ(geometry.getBoundsMaxZ());
            response.setBoundingBox(bbox);
        }

        // Thumbnail
        response.setThumbnailPath(geometry.getThumbnailPath());

        // Version info
        response.setVersion(geometry.getVersion());
        response.setIsLatest(geometry.getIsLatest());

        // Other metadata
        response.setChecksum(geometry.getChecksum());
        response.setUploadSource(geometry.getUploadSource());
        response.setNotes(geometry.getAsset().getNotes());

        // Timestamps
        response.setCreatedAt(geometry.getCreatedAt());
        response.setUpdatedAt(geometry.getUpdatedAt());

        return response;
    }

    private boolean hasAnyBoundingBoxData(Geometry geometry) {
        return geometry.getBoundsMinX() != null ||
                geometry.getBoundsMinY() != null ||
                geometry.getBoundsMinZ() != null ||
                geometry.getBoundsMaxX() != null ||
                geometry.getBoundsMaxY() != null ||
                geometry.getBoundsMaxZ() != null;
    }
}