package com.andybui.rmlmanager.controller;

import com.andybui.rmlmanager.dto.Model3DRequest;
import com.andybui.rmlmanager.model.Model3D;
import com.andybui.rmlmanager.service.Model3DService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/models")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class Model3DController {

    private final Model3DService service;

    @GetMapping
    public ResponseEntity<List<Model3D>> getAllModels() {
        return ResponseEntity.ok(service.getAllModels());
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Model3D> getModelById(@PathVariable String itemId) {
        return service.getModelById(itemId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Model3D> createModel(@Valid @RequestBody Model3DRequest request) {
        Model3D created = service.createModel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<Model3D> updateModel(
            @PathVariable String itemId,
            @Valid @RequestBody Model3DRequest request) {
        try {
            Model3D updated = service.updateModel(itemId, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteModel(@PathVariable String itemId) {
        service.deleteModel(itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Model3D>> getModelsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(service.getModelsByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Model3D>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(service.searchByName(name));
    }
}