package com.jp.daichi.designer.ingame.manager;

import com.jp.daichi.designer.ingame.InGameMaterial;
import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.simple.manager.SimpleMaterialManager;

import java.util.Map;
import java.util.UUID;

/**
 * ゲーム用のマテリアルマネージャーの実装
 */
public class InGameMaterialManager extends SimpleMaterialManager {
    @Override
    public Material deserializeManagedObject(Map<String, Object> map) {
        InGameMaterial material = InGameMaterial.deserialize(this, map);
        if (material != null) {
            addInstance(material);
        }
        return material;
    }

    @Override
    public Material createInstance(String name) {
        Material material = new InGameMaterial(name, UUID.randomUUID(), this);
        addInstance(material);
        return material;
    }
}
