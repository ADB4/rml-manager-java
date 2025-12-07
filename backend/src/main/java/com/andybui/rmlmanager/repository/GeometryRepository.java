package com.andybui.rmlmanager.repository;

import com.andybui.rmlmanager.model.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeometryRepository extends JpaRepository<Geometry, String> {

    @Query("SELECT g FROM Geometry g WHERE g.asset.id = :assetId")
    List<Geometry> findByAssetId(@Param("assetId") String assetId);

    @Query("SELECT g FROM Geometry g WHERE g.asset.id = :assetId AND g.isLatest = :isLatest")
    List<Geometry> findByAssetIdAndIsLatest(@Param("assetId") String assetId, @Param("isLatest") Boolean isLatest);

}