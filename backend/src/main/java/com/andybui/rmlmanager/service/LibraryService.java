package com.andybui.rmlmanager.service;

import com.andybui.rmlmanager.dto.AssetDto;
import com.andybui.rmlmanager.dto.LibraryItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LibraryService {

    private final S3StorageService s3StorageService;
    private final AssetService assetService;

    /**
     * Load library from any S3 bucket
     */
    public List<LibraryItemDto> loadLibraryFromS3(String bucketName, String key, String region) {
        log.info("Loading library from S3: s3://{}/{}", bucketName, key);
        return s3StorageService.getJsonArrayFromS3(bucketName, key, region, LibraryItemDto.class);
    }

    /**
     * Load library from default/private S3 bucket (uses configured bucket)
     */
    public List<LibraryItemDto> loadLibraryFromDefaultBucket(String s3Key) {
        log.info("Loading library from default S3 bucket: {}", s3Key);
        return s3StorageService.getJsonArrayFromS3(s3Key, LibraryItemDto.class);
    }

    /**
     * Get single item from any S3 bucket
     */
    public LibraryItemDto getLibraryItem(String bucketName, String key, String region, String itemCode) {
        List<LibraryItemDto> items = loadLibraryFromS3(bucketName, key, region);
        return findItem(items, itemCode);
    }

    /**
     * Get single item from default bucket
     */
    public LibraryItemDto getLibraryItemFromDefaultBucket(String s3Key, String itemCode) {
        List<LibraryItemDto> items = loadLibraryFromDefaultBucket(s3Key);
        return findItem(items, itemCode);
    }

    /**
     * Import library from any S3 bucket as assets
     */
    @Transactional
    public ImportResult importLibraryFromS3(String bucketName, String key, String region) {
        List<LibraryItemDto> libraryItems = loadLibraryFromS3(bucketName, key, region);
        String source = "s3://" + bucketName + "/" + key;
        return importItems(libraryItems, source);
    }

    /**
     * Import library from default bucket as assets
     */
    @Transactional
    public ImportResult importLibraryFromDefaultBucket(String s3Key) {
        List<LibraryItemDto> libraryItems = loadLibraryFromDefaultBucket(s3Key);
        return importItems(libraryItems, s3Key);
    }

    /**
     * Common import logic
     */
    private ImportResult importItems(List<LibraryItemDto> libraryItems, String source) {
        List<String> importedIds = new ArrayList<>();
        List<String> failedItems = new ArrayList<>();
        List<String> skippedItems = new ArrayList<>();

        for (LibraryItemDto item : libraryItems) {
            try {
                // Check if already exists
                if (assetService.existsByAssetCode(item.getItemCode())) {
                    log.debug("Skipping existing asset: {}", item.getItemCode());
                    skippedItems.add(item.getItemCode());
                    continue;
                }

                AssetDto assetDto = convertToAssetDto(item);
                var created = assetService.createAsset(assetDto);
                importedIds.add(created.getId());

                log.info("Imported library item as asset: {} ({})", item.getItemCode(), created.getId());

            } catch (Exception ex) {
                log.error("Failed to import library item: {}", item.getItemCode(), ex);
                failedItems.add(item.getItemCode() + ": " + ex.getMessage());
            }
        }

        log.info("Import from {} completed: {} imported, {} skipped, {} failed",
                source, importedIds.size(), skippedItems.size(), failedItems.size());

        return new ImportResult(source, importedIds, skippedItems, failedItems);
    }


    private LibraryItemDto findItem(List<LibraryItemDto> items, String itemCode) {
        return items.stream()
                .filter(item -> item.getItemCode().equals(itemCode))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Library item not found: " + itemCode));
    }

    private AssetDto convertToAssetDto(LibraryItemDto item) {
        AssetDto assetDto = new AssetDto();

        assetDto.setAssetCode(item.getItemCode());
        assetDto.setAssetName(item.getItemName());
        assetDto.setCategory(item.getCategory());
        assetDto.setSubcategory(item.getSubcategory());
        assetDto.setDescription(item.getDescription());
        assetDto.setNotes(item.getCreatorNote());
        assetDto.setShader(item.getShader());
        assetDto.setMaterial(item.getMaterial());
        assetDto.setAnimation("true".equalsIgnoreCase(item.getModelAnimation()));

        // Parse LOD counts
        List<Integer> lods = parseLodCounts(item.getLodCount());
        assetDto.setLods(lods);

        return assetDto;
    }

    /**
     * Parse LOD count string like "3346,2708,116" into List of Integers
     */
    private List<Integer> parseLodCounts(String lodCountString) {
        if (lodCountString == null || lodCountString.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return java.util.Arrays.stream(lodCountString.split(","))
                .map(String::trim)
                .map(s -> s.replaceAll("[^0-9]", ""))
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    /**
     * Import result DTO
     */
    public static class ImportResult {
        public final String source;
        public final List<String> importedIds;
        public final List<String> skippedItems;
        public final List<String> failedItems;

        public ImportResult(String source, List<String> importedIds, List<String> skippedItems, List<String> failedItems) {
            this.source = source;
            this.importedIds = importedIds;
            this.skippedItems = skippedItems;
            this.failedItems = failedItems;
        }

        public int getImportedCount() {
            return importedIds.size();
        }

        public int getSkippedCount() {
            return skippedItems.size();
        }

        public int getFailedCount() {
            return failedItems.size();
        }

        public int getTotalCount() {
            return importedIds.size() + skippedItems.size() + failedItems.size();
        }
    }
}