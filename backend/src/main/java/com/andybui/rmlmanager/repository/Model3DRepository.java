package com.andybui.rmlmanager.repository;

import com.andybui.rmlmanager.model.Model3D;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Model3DRepository extends JpaRepository<Model3D, String> {

    // Custom query methods
    List<Model3D> findByCategory(String category);

    List<Model3D> findByCategoryAndSubcategory(String category, String subcategory);

    List<Model3D> findByAnimation(Boolean animation);

    List<Model3D> findByItemNameContainingIgnoreCase(String itemName);
}