package com.andybui.rmlmanager.controller;

import com.andybui.rmlmanager.dto.GeometryResponseDto;
import com.andybui.rmlmanager.dto.GeometryUploadDto;
import com.andybui.rmlmanager.service.GeometryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/geometries")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class GeometryController {

    private final GeometryService geometryService;
    private final ObjectMapper objectMapper;

    @PostMapping("/upload")
    public ResponseEntity<GeometryResponseDto> uploadGeometry(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "metadata", required = false) String metadataJson) {

        log.info("Upload request - File: {}", file.getOriginalFilename());

        // Parse metadata JSON if provided
        GeometryUploadDto request = null;
        if (metadataJson != null && !metadataJson.isEmpty()) {
            try {
                request = objectMapper.readValue(metadataJson, GeometryUploadDto.class);
            } catch (Exception e) {
                log.error("Failed to parse metadata JSON", e);
                throw new IllegalArgumentException("Invalid metadata JSON: " + e.getMessage());
            }
        }

        if (request == null) {
            throw new IllegalArgumentException("Metadata with assetId is required");
        }

        GeometryResponseDto geometry = geometryService.uploadGeometry(file, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(geometry);
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<GeometryResponseDto>> getGeometriesByAsset(@PathVariable String assetId) {
        log.info("GET /api/geometries/asset/{}", assetId);
        return ResponseEntity.ok(geometryService.getGeometriesByAsset(assetId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeometryResponseDto> getGeometry(@PathVariable String id) {
        log.info("GET /api/geometries/{}", id);
        return ResponseEntity.ok(geometryService.getGeometryById(id));
    }

    @GetMapping("/{id}/download-url")
    public ResponseEntity<Map<String, String>> getDownloadUrl(@PathVariable String id) {
        log.info("GET /api/geometries/{}/download-url", id);
        String url = geometryService.getDownloadUrl(id);
        return ResponseEntity.ok(Map.of(
                "url", url,
                "expiresIn", "60 minutes"
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGeometry(@PathVariable String id) {
        log.info("DELETE /api/geometries/{}", id);
        geometryService.deleteGeometry(id);
        return ResponseEntity.noContent().build();
    }
}