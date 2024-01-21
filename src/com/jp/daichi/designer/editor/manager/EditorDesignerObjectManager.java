package com.jp.daichi.designer.editor.manager;

import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.editor.DesignerObjectSerializer;
import com.jp.daichi.designer.editor.EditorImageObject;
import com.jp.daichi.designer.simple.manager.SimpleDesignerObjectManager;

import java.util.Map;
import java.util.UUID;

public class EditorDesignerObjectManager extends SimpleDesignerObjectManager {
    private final History history;
    private Canvas canvas;

    public EditorDesignerObjectManager(History history) {
        this.history = history;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public DesignerObject deserializeManagedObject(Map<String, Object> map) {
        DesignerObjectSerializer.DeserializedData deserializedData = DesignerObjectSerializer.deserialize(map);
        if (deserializedData != null) {
            DesignerObject designerObject = null;
            if (deserializedData.type() == DesignerObjectType.IMAGE) {
                designerObject = EditorImageObject.deserialize(history, canvas,deserializedData, map);
            } else {
                //TODO
            }
            if (designerObject != null) {
                addInstance(designerObject);
            }
            return designerObject;
        } else {
            return null;
        }
    }


    @Override
    public <T extends DesignerObject> T createInstance(String name, DesignerObjectType type) {
        String resolvedName = resolveName(null,name);
        DesignerObject designerObject = null;
        switch (type) {
            case IMAGE -> {
                designerObject = new EditorImageObject(history,resolvedName, UUID.randomUUID(),canvas,new Point(0,0),new SignedDimension(0,0));
            }
            case MARKER -> {
                designerObject = null;
            }
            case COLLISION -> {
                designerObject = null;
            }

        }
        if (designerObject != null) {
            addInstance(designerObject);
            return (T) designerObject;
        } else {
            return null;
        }
    }
}
