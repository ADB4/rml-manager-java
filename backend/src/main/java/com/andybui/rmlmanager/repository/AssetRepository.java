package com.andybui.rmlmanager.repository;

import com.andybui.rmlmanager.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String> {

    // Custom query methods
    List<Asset> findByCategory(String category);

    List<Asset> findByCategoryAndSubcategory(String category, String subcategory);

    List<Asset> findByAnimation(Boolean animation);

    List<Asset> findByItemNameContainingIgnoreCase(String itemName);
}