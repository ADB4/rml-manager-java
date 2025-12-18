package com.andybui.rmlmanager.controller;

import com.andybui.rmlmanager.dto.LibraryItemDto;
import com.andybui.rmlmanager.service.S3StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test-lib")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class TestLibraryController {

    private final S3StorageService s3StorageService;
    private final ObjectMapper objectMapper;

    @GetMapping("/step1")
    public ResponseEntity<Map<String, Object>> testStep1() {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("Step 1: Testing basic response");
            result.put("success", true);
            result.put("message", "Basic endpoint works");
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Step 1 failed", ex);
            result.put("success", false);
            result.put("error", ex.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/step2")
    public ResponseEntity<Map<String, Object>> testStep2() {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("Step 2: Testing S3 text download");
            String text = s3StorageService.getTextFromS3(
                    "static.rogerlib.com",
                    "static/models/json/models.json",
                    "us-east-2"
            );
            result.put("success", true);
            result.put("textLength", text.length());
            result.put("preview", text.substring(0, Math.min(200, text.length())));
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Step 2 failed", ex);
            result.put("success", false);
            result.put("error", ex.getMessage());
            result.put("stackTrace", ex.getStackTrace()[0].toString());
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/step3")
    public ResponseEntity<Map<String, Object>> testStep3() {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("Step 3: Testing JSON parsing");
            String text = s3StorageService.getTextFromS3(
                    "static.rogerlib.com",
                    "static/models/json/models.json",
                    "us-east-2"
            );

            log.info("Parsing JSON...");
            List<LibraryItemDto> items = objectMapper.readValue(
                    text,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, LibraryItemDto.class)
            );

            log.info("Parsed {} items", items.size());
            result.put("success", true);
            result.put("itemCount", items.size());
            result.put("firstItem", items.get(0).getItemCode());
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Step 3 failed", ex);
            result.put("success", false);
            result.put("error", ex.getMessage());
            result.put("type", ex.getClass().getName());
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/step4")
    public ResponseEntity<Map<String, Object>> testStep4() {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("Step 4: Testing full getJsonArrayFromS3");
            List<LibraryItemDto> items = s3StorageService.getJsonArrayFromS3(
                    "static.rogerlib.com",
                    "static/models/json/models.json",
                    "us-east-2",
                    LibraryItemDto.class
            );

            result.put("success", true);
            result.put("itemCount", items.size());
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Step 4 failed", ex);
            result.put("success", false);
            result.put("error", ex.getMessage());
            result.put("type", ex.getClass().getName());
            return ResponseEntity.ok(result);
        }
    }
}