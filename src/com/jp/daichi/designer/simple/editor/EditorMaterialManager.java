package com.jp.daichi.designer.simple.editor;

import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.simple.SimpleMaterial;
import com.jp.daichi.designer.simple.SimpleMaterialManager;

import java.util.UUID;

public class EditorMaterialManager extends SimpleMaterialManager {
    @Override
    public Material addMaterial(String name) {
        String resolvedName = resolveName(name);
        Material material = new EditorMaterial(resolvedName, UUID.randomUUID());
        material.setUpdateObserver(getUpdateObserver());
        materials.add(material);
        sendUpdate(UpdateAction.ADD_MATERIAL);
        return material;
    }
}
