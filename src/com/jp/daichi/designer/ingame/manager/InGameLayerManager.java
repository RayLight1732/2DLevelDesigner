package com.jp.daichi.designer.ingame.manager;

import com.jp.daichi.designer.ingame.InGameLayer;
import com.jp.daichi.designer.interfaces.DesignerObjectType;
import com.jp.daichi.designer.interfaces.Layer;
import com.jp.daichi.designer.simple.manager.SimpleLayerManager;

import java.util.Map;
import java.util.UUID;

/**
 * ゲーム用のレイヤーマネージャーの実装
 */
public class InGameLayerManager extends SimpleLayerManager {
    @Override
    public Layer deserializeManagedObject(Map<String, Object> map) {
        Layer layer = InGameLayer.deserialize(map);
        addInstance(layer);
        return layer;
    }

    @Override
    public Layer createInstance(String name, DesignerObjectType type) {
        Layer layer = new InGameLayer(resolveName(null,name), UUID.randomUUID(),type);
        addInstance(layer);
        return layer;
    }
}
