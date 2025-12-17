package com.andybui.rmlmanager.model;

import java.util.Arrays;

public enum TextureMapType {
    ALBEDO("albedo"),
    NORMAL("normal"),
    ROUGHNESS("roughness"),
    METALLIC("metallic"),
    DISPLACEMENT("displacement"),
    EMISSIVE("emissive"),
    OPACITY("opacity");

    private final String type;
    TextureMapType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static boolean isSupported(String map) {
        return Arrays.stream(values())
                .anyMatch(type -> type.type.equals(map));
    }
}
