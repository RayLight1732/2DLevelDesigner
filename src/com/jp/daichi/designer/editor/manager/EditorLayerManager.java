package com.jp.daichi.designer.editor.manager;

import com.jp.daichi.designer.editor.EditorLayer;
import com.jp.daichi.designer.interfaces.DesignerObjectType;
import com.jp.daichi.designer.interfaces.Layer;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.simple.manager.SimpleLayerManager;

import java.util.Map;
import java.util.UUID;

/**
 * エディタ用のレイヤーマネージャー
 */
public class EditorLayerManager extends SimpleLayerManager {

    private final History history;

    /**
     * 新しいレイヤーマネージャーのインスタンスを作成する
     *
     * @param history 履歴
     */
    public EditorLayerManager(History history) {
        this.history = history;
    }

    @Override
    public Layer deserializeManagedObject(Map<String, Object> map) {
        Layer layer = EditorLayer.deserialize(history, map);
        if (layer != null) {
            addInstance(layer);
        }
        return layer;
    }


    @Override
    public Layer createInstance(String name, DesignerObjectType type) {
        Layer layer = new EditorLayer(history, resolveName(null, name), UUID.randomUUID(), type);
        addInstance(layer);
        return layer;
    }
}
