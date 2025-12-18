package com.andybui.rmlmanager.controller;

import com.andybui.rmlmanager.dto.LibraryItemDto;
import com.andybui.rmlmanager.service.LibraryService;
import com.andybui.rmlmanager.service.LibraryService.ImportResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class LibraryController {

    private final LibraryService libraryService;

    // Default library location constants
    private static final String DEFAULT_BUCKET = "static.rogerlib.com";
    private static final String DEFAULT_KEY = "static/models/json/models.json";
    private static final String DEFAULT_REGION = "us-east-2";

    /**
     * Load library from S3
     * GET /api/library (uses defaults)
     * GET /api/library?bucket=my-bucket&key=data.json&region=us-east-1 (custom)
     */
    @GetMapping
    public ResponseEntity<List<LibraryItemDto>> getLibrary(
            @RequestParam(required = false) String bucket,
            @RequestParam(required = false) String key,
            @RequestParam(required = false) String region) {

        // Use defaults if not provided
        String finalBucket = bucket != null ? bucket : DEFAULT_BUCKET;
        String finalKey = key != null ? key : DEFAULT_KEY;
        String finalRegion = region != null ? region : DEFAULT_REGION;

        log.info("GET /api/library - Loading from s3://{}/{}", finalBucket, finalKey);
        List<LibraryItemDto> items = libraryService.loadLibraryFromS3(finalBucket, finalKey, finalRegion);
        return ResponseEntity.ok(items);
    }

    /**
     * Load library from default/configured S3 bucket
     * GET /api/library/default?key=library/items.json
     */
    @GetMapping("/default")
    public ResponseEntity<List<LibraryItemDto>> getLibraryFromDefaultBucket(@RequestParam String key) {
        log.info("GET /api/library/default - Loading library from default bucket: {}", key);
        List<LibraryItemDto> items = libraryService.loadLibraryFromDefaultBucket(key);
        return ResponseEntity.ok(items);
    }

    /**
     * Get single item from any S3 bucket
     * GET /api/library/item?bucket=my-bucket&key=data.json&region=us-east-1&code=ac0d
     */
    @GetMapping("/item")
    public ResponseEntity<LibraryItemDto> getLibraryItemFromS3(
            @RequestParam String bucket,
            @RequestParam String key,
            @RequestParam String region,
            @RequestParam String code) {
        log.info("GET /api/library/item - Loading item {} from s3://{}/{}", code, bucket, key);
        LibraryItemDto item = libraryService.getLibraryItem(bucket, key, region, code);
        return ResponseEntity.ok(item);
    }

    /**
     * Get single item from default bucket
     * GET /api/library/default/item?key=library/items.json&code=ac0d
     */
    @GetMapping("/default/item")
    public ResponseEntity<LibraryItemDto> getLibraryItemFromDefaultBucket(
            @RequestParam String key,
            @RequestParam String code) {
        log.info("GET /api/library/default/item - Loading item {} from default bucket: {}", code, key);
        LibraryItemDto item = libraryService.getLibraryItemFromDefaultBucket(key, code);
        return ResponseEntity.ok(item);
    }

    /**
     * Import library from any S3 bucket as assets
     * POST /api/library/import?bucket=my-bucket&key=data.json&region=us-east-1
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importLibraryFromS3(
            @RequestParam String bucket,
            @RequestParam String key,
            @RequestParam String region) {
        log.info("POST /api/library/import - Importing from s3://{}/{}", bucket, key);
        ImportResult result = libraryService.importLibraryFromS3(bucket, key, region);

        return ResponseEntity.ok(buildImportResponse(result));
    }

    /**
     * Import library from default bucket as assets
     * POST /api/library/default/import?key=library/items.json
     */
    @PostMapping("/default/import")
    public ResponseEntity<Map<String, Object>> importLibraryFromDefaultBucket(@RequestParam String key) {
        log.info("POST /api/library/default/import - Importing from default bucket: {}", key);
        ImportResult result = libraryService.importLibraryFromDefaultBucket(key);

        return ResponseEntity.ok(buildImportResponse(result));
    }

    /**
     * Build import response with detailed statistics
     */
    private Map<String, Object> buildImportResponse(ImportResult result) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Import completed");
        response.put("source", result.source);
        response.put("statistics", Map.of(
                "total", result.getTotalCount(),
                "imported", result.getImportedCount(),
                "skipped", result.getSkippedCount(),
                "failed", result.getFailedCount()
        ));
        response.put("importedAssetIds", result.importedIds);

        if (!result.failedItems.isEmpty()) {
            response.put("failedItems", result.failedItems);
        }
        if (!result.skippedItems.isEmpty()) {
            response.put("skippedItems", result.skippedItems);
        }

        return response;
    }
}