package com.jp.daichi.designer.ingame;

import com.jp.daichi.designer.interfaces.manager.MaterialManager;
import com.jp.daichi.designer.simple.SimpleMaterial;

import java.util.UUID;

/**
 * ゲーム用のマテリアルの実装
 */
public class InGameMaterial extends SimpleMaterial {
    public InGameMaterial(String name, UUID uuid, MaterialManager materialManager) {
        super(name, uuid, materialManager);
    }
}
