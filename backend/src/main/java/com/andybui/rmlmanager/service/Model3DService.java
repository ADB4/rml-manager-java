package com.andybui.rmlmanager.service;

import com.andybui.rmlmanager.dto.Model3DRequest;
import com.andybui.rmlmanager.model.Model3D;
import com.andybui.rmlmanager.repository.Model3DRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Model3DService {

    private final Model3DRepository repository;

    @Transactional(readOnly = true)
    public List<Model3D> getAllModels() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Model3D> getModelById(String itemId) {
        return repository.findById(itemId);
    }

    @Transactional
    public Model3D createModel(Model3DRequest request) {
        Model3D model = new Model3D();
        model.setItemId(request.getItemId());
        model.setItemName(request.getItemName());
        model.setCategory(request.getCategory());
        model.setSubcategory(request.getSubcategory());
        model.setDescription(request.getDescription());
        model.setNotes(request.getNotes());
        model.setShader(request.getShader());
        model.setMaterial(request.getMaterial());
        model.setAnimation(request.getAnimation());
        model.setLods(request.getLods());
        return repository.save(model);
    }

    @Transactional
    public Model3D updateModel(String itemId, Model3DRequest request) {
        Model3D model = repository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Model not found with id: " + itemId));

        model.setItemName(request.getItemName());
        model.setCategory(request.getCategory());
        model.setSubcategory(request.getSubcategory());
        model.setDescription(request.getDescription());
        model.setNotes(request.getNotes());
        model.setShader(request.getShader());
        model.setMaterial(request.getMaterial());
        model.setAnimation(request.getAnimation());
        model.setLods(request.getLods());

        return repository.save(model);
    }

    @Transactional
    public void deleteModel(String itemId) {
        repository.deleteById(itemId);
    }

    @Transactional(readOnly = true)
    public List<Model3D> getModelsByCategory(String category) {
        return repository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<Model3D> searchByName(String name) {
        return repository.findByItemNameContainingIgnoreCase(name);
    }
}