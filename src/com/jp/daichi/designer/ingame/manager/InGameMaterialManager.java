package com.jp.daichi.designer.ingame.manager;

import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.simple.SimpleMaterial;
import com.jp.daichi.designer.simple.manager.SimpleMaterialManager;

import java.util.Map;
import java.util.UUID;

/**
 * ゲーム用のマテリアルマネージャーの実装
 */
public class InGameMaterialManager extends SimpleMaterialManager {
    @Override
    public Material deserializeManagedObject(Map<String, Object> map) {
        return null;//TODO
    }

    @Override
    public Material createInstance(String name) {
        Material material = new SimpleMaterial(name, UUID.randomUUID(),this);
        addInstance(material);
        return material;
    }
}
