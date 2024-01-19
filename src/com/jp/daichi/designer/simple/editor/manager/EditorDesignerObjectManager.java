package com.jp.daichi.designer.simple.editor.manager;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.simple.editor.EditorImageObject;
import com.jp.daichi.designer.simple.manager.SimpleDesignerObjectManager;

import java.util.Map;

public class EditorDesignerObjectManager extends SimpleDesignerObjectManager {
    private final History history;
    private final Canvas canvas;

    public EditorDesignerObjectManager(History history,Canvas canvas) {
        this.history = history;
        this.canvas = canvas;
    }

    @Override
    public DesignerObject deserializeManagedObject(Map<String, Object> map) {
        if (map.get("Type").equals("Image")) {
            return EditorImageObject.deserialize(history,canvas,map);
        } else {
            //TODO
            return null;
        }
    }

    @Override
    protected DesignerObject createManagedObjectInstance(String resolvedName) {
        return null;
    }
}
