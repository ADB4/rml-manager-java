package com.andybui.rmlmanager.service;

import com.andybui.rmlmanager.dto.AssetRequest;
import com.andybui.rmlmanager.model.Asset;
import com.andybui.rmlmanager.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository repository;

    @Transactional(readOnly = true)
    public List<Asset> getAllAssets() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Asset> getAssetById(String itemId) {
        return repository.findById(itemId);
    }

    @Transactional
    public Asset createAsset(AssetRequest request) {
        Asset asset = new Asset();
        asset.setItemId(request.getItemId());
        asset.setItemName(request.getItemName());
        asset.setCategory(request.getCategory());
        asset.setSubcategory(request.getSubcategory());
        asset.setDescription(request.getDescription());
        asset.setNotes(request.getNotes());
        asset.setShader(request.getShader());
        asset.setMaterial(request.getMaterial());
        asset.setAnimation(request.getAnimation());
        asset.setLods(request.getLods());
        return repository.save(asset);
    }

    @Transactional
    public Asset updateAsset(String itemId, AssetRequest request) {
        Asset asset = repository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("asset not found with id: " + itemId));

        asset.setItemName(request.getItemName());
        asset.setCategory(request.getCategory());
        asset.setSubcategory(request.getSubcategory());
        asset.setDescription(request.getDescription());
        asset.setNotes(request.getNotes());
        asset.setShader(request.getShader());
        asset.setMaterial(request.getMaterial());
        asset.setAnimation(request.getAnimation());
        asset.setLods(request.getLods());

        return repository.save(asset);
    }

    @Transactional
    public void deleteAsset(String itemId) {
        repository.deleteById(itemId);
    }

    @Transactional(readOnly = true)
    public List<Asset> getAssetsByCategory(String category) {
        return repository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<Asset> searchByName(String name) {
        return repository.findByItemNameContainingIgnoreCase(name);
    }
}