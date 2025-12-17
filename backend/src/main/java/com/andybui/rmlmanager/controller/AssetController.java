package com.andybui.rmlmanager.controller;

import com.andybui.rmlmanager.dto.AssetDto;
import com.andybui.rmlmanager.model.Asset;
import com.andybui.rmlmanager.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class AssetController {

    private final AssetService service;

    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        log.info("GET /api/assets - Fetching all assets");
        return ResponseEntity.ok(service.getAllAssets());
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Asset> getAssetById(@PathVariable String itemId) {
        log.info("GET /api/assets/itemId} - Fetching asset", itemId);
        return ResponseEntity.ok(service.getAssetById(itemId));
    }

    @PostMapping
    public ResponseEntity<Asset> createAsset(@Valid @RequestBody AssetDto request) {
        log.info("POST /api/assets - Creating new asset");
        Asset created = service.createAsset(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<Asset> updateAsset(
            @PathVariable String itemId,
            @Valid @RequestBody AssetDto request) {
        log.info("PUT /api/assets/{} - Updating asset", itemId);
        Asset updated = service.updateAsset(itemId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteAsset(@PathVariable String itemId) {
        log.info("DELETE /api/assets/{} - Deleting asset", itemId);
        service.deleteAsset(itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Asset>> getAssetsByCategory(@PathVariable String category) {
        log.info("GET /api/assets/category/{} - Fetching assets by category", category);
        return ResponseEntity.ok(service.getAssetsByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Asset>> searchByName(@RequestParam String name) {
        log.info("GET /api/assets/search?name={} - Searching assets", name);
        return ResponseEntity.ok(service.searchByName(name));
    }

    @GetMapping("/animation/{hasAnimation}")
    public ResponseEntity<List<Asset>> getAssetsByAnimation(@PathVariable Boolean hasAnimation) {
        log.info("GET /api/assets/animation/{} - Fetching assets by animation", hasAnimation);
        return ResponseEntity.ok(service.getAssetsByAnimation(hasAnimation));
    }
}