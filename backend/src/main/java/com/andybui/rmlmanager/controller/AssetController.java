package com.andybui.rmlmanager.controller;

import com.andybui.rmlmanager.dto.AssetRequest;
import com.andybui.rmlmanager.model.Asset;
import com.andybui.rmlmanager.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AssetController {

    private final AssetService service;

    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        return ResponseEntity.ok(service.getAllAssets());
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Asset> getAssetById(@PathVariable String itemId) {
        return service.getAssetById(itemId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Asset> createAsset(@Valid @RequestBody AssetRequest request) {
        Asset created = service.createAsset(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<Asset> updateAsset(
            @PathVariable String itemId,
            @Valid @RequestBody AssetRequest request) {
        try {
            Asset updated = service.updateAsset(itemId, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteAsset(@PathVariable String itemId) {
        service.deleteAsset(itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Asset>> getAssetsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(service.getAssetsByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Asset>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(service.searchByName(name));
    }
}