package com.andybui.rmlmanager.model;

import java.util.Arrays;

public enum TextureFileType {
    JPG("jpg", "image/jpeg"),
    GIF("gif", "image/fig"),
    PNG("png", "image/png"),
    SVG("svg", "image/svg+xml"),
    WEBP("webp", "image/webp"),
    AVIF("avif", "image/avif");

    private final String extension;
    private final String mimeType;

    TextureFileType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }
    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }
    public static TextureFileType fromExtension(String ext) {
        String normalized = ext.toLowerCase().replace(".", "");
        for (TextureFileType type : values()) {
            if (type.extension.equals(normalized)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported file extension: " + ext);
    }

    public static boolean isSupported(String ext) {
        String normalized = ext.toLowerCase().replace(".", "");
        return Arrays.stream(values())
                .anyMatch(type -> type.extension.equals(normalized));
    }
}
