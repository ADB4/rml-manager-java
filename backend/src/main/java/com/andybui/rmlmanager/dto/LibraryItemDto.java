package com.andybui.rmlmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryItemDto {

    @JsonProperty("itemcode")
    private String itemCode;

    @JsonProperty("itemname")
    private String itemName;

    private String category;
    private String subcategory;
    private String material;

    private List<List<String>> models;

    @JsonProperty("modelanimation")
    private String modelAnimation;

    private String zoom;

    @JsonProperty("texturemap")
    private Map<String, String> textureMap;

    @JsonProperty("texturesets")
    private List<List<TextureSet>> textureSets;

    private String colors;

    @JsonProperty("lodcount")
    private String lodCount;

    private String description;

    @JsonProperty("creatornote")
    private String creatorNote;

    private String shader;
    private String version;

    private List<String> lods;

    @JsonProperty("lodmap")
    private Map<String, String> lodMap;

    @JsonProperty("polycount")
    private Map<String, String> polyCount;

    @JsonProperty("colorcodes")
    private List<String> colorCodes;

    @JsonProperty("colormap")
    private Map<String, String> colorMap;

    @JsonProperty("obfuscatedpath")
    private String obfuscatedPath;

    private String download;
    private String preview;

    @JsonProperty("imagepath")
    private String imagePath;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextureSet {
        private String id;
        private String alpha;
        private String animation;
        private String displacement;

        @JsonProperty("uniquevariants")
        private String uniqueVariants;
    }
}