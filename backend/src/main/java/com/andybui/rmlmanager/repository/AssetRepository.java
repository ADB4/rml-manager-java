package com.andybui.rmlmanager.repository;

import com.andybui.rmlmanager.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String> {

    @Query("SELECT a FROM Asset a WHERE a.category = :category")
    List<Asset> findByCategory(@Param("category") String category);

    @Query("SELECT a FROM Asset a WHERE LOWER(a.assetName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Asset> findByItemNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT a FROM Asset a WHERE a.animation = :animation")
    List<Asset> findByAnimation(@Param("animation") Boolean animation);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Asset a WHERE a.assetCode = :assetCode")
    boolean existsByAssetCode(@Param("assetCode") String assetCode);
}