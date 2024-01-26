package com.jp.daichi.designer.interfaces;

public enum DesignerObjectType {
    IMAGE("Image"), COLLISION("Collision"), MARKER("Marker");

    private final String displayName;

    DesignerObjectType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
