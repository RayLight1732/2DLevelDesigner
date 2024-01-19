package com.jp.daichi.designer.simple.editor.manager;

import com.jp.daichi.designer.interfaces.Layer;
import com.jp.daichi.designer.interfaces.UpdateObserver;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.simple.editor.EditorLayer;
import com.jp.daichi.designer.simple.manager.SimpleLayerManager;

import java.util.Map;
import java.util.UUID;

/**
 * エディタ用のレイヤーマネージャー
 */
public class EditorLayerManager extends SimpleLayerManager {

    private final History history;

    public EditorLayerManager(History history) {
        this.history = history;
    }

    @Override
    public Layer deserializeManagedObject(Map<String, Object> map) {
        return EditorLayer.deserialize(history,map);
    }

    @Override
    protected Layer createManagedObjectInstance(String resolvedName) {
        return new EditorLayer(history,resolvedName, UUID.randomUUID());
    }

}
