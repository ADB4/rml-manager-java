package com.andybui.rmlmanager.service;

import com.andybui.rmlmanager.config.S3StorageProperties;
import com.andybui.rmlmanager.exception.ResourceNotFoundException;
import com.andybui.rmlmanager.model.Asset;
import com.andybui.rmlmanager.model.GeometryFileType;
import com.andybui.rmlmanager.model.Geometry;
import com.andybui.rmlmanager.repository.AssetRepository;
import com.andybui.rmlmanager.repository.GeometryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeometryService {

    private final GeometryRepository geometryRepository;
    private final AssetRepository assetRepository;
    private final S3StorageService s3StorageService;
    private final S3StorageProperties s3Properties;

    @Transactional
    public Geometry uploadGeometry(MultipartFile file, String assetId, String notes) {
        log.info("Uploading geometry file for asset: {}", assetId);

        // Validate asset exists
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));

        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Get file extension and validate
        String originalFileName = file.getOriginalFilename();
        String extension = getFileExtension(originalFileName);

        if (!GeometryFileType.isSupported(extension)) {
            throw new IllegalArgumentException("Unsupported file type: " + extension);
        }

        GeometryFileType fileType = GeometryFileType.fromExtension(extension);

        // Upload to S3
        String s3Key = s3StorageService.uploadFile(file, assetId);

        // Get current user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //String uploadedBy = auth != null ? auth.getName() : "system";

        // Create geometry record
        Geometry geometry = new Geometry();
        geometry.setAsset(asset);
        geometry.setFileName(s3Key.substring(s3Key.lastIndexOf("/") + 1));
        geometry.setFileName(originalFileName);
        geometry.setFileType(fileType);
        geometry.setS3Key(s3Key);
        geometry.setS3Bucket(s3Properties.getBucketName());
        geometry.setFileSize(file.getSize());
        geometry.setContentType(file.getContentType());
        //geometry.setUploadedBy(uploadedBy);
        geometry.setUploadSource("web_ui");

        Geometry saved = geometryRepository.save(geometry);
        log.info("Geometry uploaded successfully: {}", saved.getId());

        return saved;
    }

    @Transactional(readOnly = true)
    public Geometry getGeometryById(String id) {
        return geometryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Geometry not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Geometry> getGeometriesByAsset(String assetId) {
        return geometryRepository.findByAssetId(assetId);
    }

    @Transactional(readOnly = true)
    public String getDownloadUrl(String id) {
        Geometry geometry = getGeometryById(id);
        return s3StorageService.generatePresignedUrl(geometry.getS3Key());
    }

    @Transactional
    public void deleteGeometry(String id) {
        Geometry geometry = getGeometryById(id);

        // Delete from S3
        try {
            s3StorageService.deleteFile(geometry.getS3Key());
        } catch (Exception ex) {
            log.error("Failed to delete file from S3: {}", geometry.getS3Key(), ex);
            // Continue with database deletion even if S3 deletion fails
        }

        // Delete from database
        geometryRepository.delete(geometry);
        log.info("Geometry deleted: {}", id);
    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return fileName.substring(lastIndexOf + 1);
    }
}