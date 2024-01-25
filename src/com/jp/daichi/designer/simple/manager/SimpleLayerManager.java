package com.jp.daichi.designer.simple.manager;

import com.jp.daichi.designer.interfaces.DesignerObjectType;
import com.jp.daichi.designer.interfaces.Layer;
import com.jp.daichi.designer.interfaces.manager.LayerManager;

import java.util.UUID;

/**
 * 基本的なレイヤーマネージャーの実装
 */
public abstract class SimpleLayerManager extends AManager<Layer> implements LayerManager {

    @Override
    protected String getName(Layer target) {
        return target.getName();
    }

    @Override
    protected UUID getUUID(Layer target) {
        return target.getUUID();
    }

    @Override
    protected String getDefaultName() {
        return "NewLayer";
    }

    @Override
    public Layer getLayer(DesignerObjectType type) {
        for (Layer layer:instances) {
            if (layer.getObjectType() == type) {
                return layer;
            }
        }
        return null;
    }
}
