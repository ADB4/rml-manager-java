package com.andybui.rmlmanager.service;

import com.andybui.rmlmanager.dto.AssetDto;
import com.andybui.rmlmanager.exception.ResourceAlreadyExistsException;
import com.andybui.rmlmanager.exception.ResourceNotFoundException;
import com.andybui.rmlmanager.model.Asset;
import com.andybui.rmlmanager.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetService {

    private final AssetRepository repository;

    @Transactional(readOnly = true)
    public List<Asset> getAllAssets() {
        log.debug("Fetching all assets");
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Asset getAssetById(String itemId) {
        log.debug("Fetching asset with id: {}", itemId);
        return repository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + itemId));
    }

    @Transactional
    public Asset createAsset(AssetDto request) {
        log.info("Creating new asset with id: {}", request.getAssetCode());

        if (repository.existsById(request.getAssetCode())) {
            throw new ResourceAlreadyExistsException("Asset already exists with id: " + request.getAssetCode());
        }

        Asset asset = mapToEntity(request);
        Asset saved = repository.save(asset);
        repository.flush();

        log.info("Successfully created asset with id: {}", saved.getAssetCode());
        return saved;
    }

    @Transactional
    public Asset updateAsset(String itemId, AssetDto request) {
        log.info("Updating asset with id: {}", itemId);

        Asset asset = repository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + itemId));

        updateEntityFromRequest(asset, request);
        Asset updated = repository.save(asset);

        log.info("Successfully updated asset with id: {}", itemId);
        return updated;
    }

    @Transactional
    public void deleteAsset(String itemId) {
        log.info("Deleting asset with id: {}", itemId);

        if (!repository.existsById(itemId)) {
            throw new ResourceNotFoundException("Asset not found with id: " + itemId);
        }

        repository.deleteById(itemId);
        log.info("Successfully deleted asset with id: {}", itemId);
    }
    @Transactional(readOnly = true)
    public boolean existsByAssetCode(String assetCode) {
        return repository.existsByAssetCode(assetCode);
    }
    @Transactional(readOnly = true)
    public List<Asset> getAssetsByCategory(String category) {
        log.debug("Fetching assets by category: {}", category);
        return repository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<Asset> searchByName(String name) {
        log.debug("Searching assets by name: {}", name);
        return repository.findByItemNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<Asset> getAssetsByAnimation(Boolean animation) {
        log.debug("Fetching assets by animation: {}", animation);
        return repository.findByAnimation(animation);
    }

    private Asset mapToEntity(AssetDto request) {
        Asset asset = new Asset();
        asset.setAssetCode(request.getAssetCode());
        updateEntityFromRequest(asset, request);
        return asset;
    }

    private void updateEntityFromRequest(Asset asset, AssetDto request) {
        asset.setAssetCode(request.getAssetCode());
        asset.setAssetName(request.getAssetName());
        asset.setCategory(request.getCategory());
        asset.setSubcategory(request.getSubcategory());
        asset.setDescription(request.getDescription());
        asset.setNotes(request.getNotes());
        asset.setShader(request.getShader());
        asset.setMaterial(request.getMaterial());
        asset.setAnimation(request.getAnimation());
        asset.setLods(request.getLods());
    }
}