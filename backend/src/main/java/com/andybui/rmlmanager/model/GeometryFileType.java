package com.andybui.rmlmanager.model;

import java.util.Arrays;

public enum GeometryFileType {
    FBX("fbx", "application/octet-stream"),
    GLB("glb", "model/gltf-binary"),
    GLTF("gltf", "model/gltf+json"),
    OBJ("obj", "model/obj"),
    BLEND("blend", "application/x-blender");

    private final String extension;
    private final String mimeType;

    GeometryFileType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }
    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }
    public static GeometryFileType fromExtension(String ext) {
        String normalized = ext.toLowerCase().replace(".", "");
        for (GeometryFileType type : values()) {
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
