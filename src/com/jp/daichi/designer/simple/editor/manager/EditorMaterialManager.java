package com.jp.daichi.designer.simple.editor.manager;

import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.interfaces.UpdateObserver;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.simple.editor.EditorMaterial;
import com.jp.daichi.designer.simple.manager.SimpleMaterialManager;

import java.util.Map;
import java.util.UUID;

public class EditorMaterialManager extends SimpleMaterialManager {

    private final History history;

    public EditorMaterialManager(History history) {
        this.history = history;
    }

    @Override
    public Material deserializeManagedObject(Map<String, Object> map) {
        Material material = EditorMaterial.deserialize(history,map);
        if (material != null) {
            instances.remove(material);
        }
        return material;
    }

}
