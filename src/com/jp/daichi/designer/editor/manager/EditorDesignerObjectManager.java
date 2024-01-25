package com.jp.daichi.designer.editor.manager;

import com.jp.daichi.designer.editor.EditorCollisionObject;
import com.jp.daichi.designer.editor.EditorImageObject;
import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.interfaces.editor.EditorDesignerObject;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.interfaces.editor.HistoryStaff;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.simple.DesignerObjectSerializer;
import com.jp.daichi.designer.simple.manager.SimpleDesignerObjectManager;

import java.util.Map;
import java.util.UUID;

/**
 * エディタ用のデザイナーオブジェクトマネージャーの実装
 */
public class EditorDesignerObjectManager extends SimpleDesignerObjectManager {
    private final History history;
    private Canvas canvas;
    private boolean saveHistory = true;

    public EditorDesignerObjectManager(History history) {
        this.history = history;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public <T extends DesignerObject> T createInstance(String name, DesignerObjectType type) {
        T designerObject = super.createInstance(name, type);
        if (saveHistory) {
            history.add(new EditRegisteredMaterialList(designerObject.getUUID(),designerObject.getName(),((EditorDesignerObject) designerObject).serialize(),true));
        }
        return designerObject;
    }

    @Override
    protected DesignerObject deserializeManagedObject(DesignerObjectSerializer.DeserializedData deserializedData, Map<String, Object> map) {
        if (deserializedData.type() == DesignerObjectType.IMAGE) {
            return EditorImageObject.deserialize(history, canvas,deserializedData, map);
        } else if (deserializedData.type() == DesignerObjectType.COLLISION) {
            return EditorCollisionObject.deserialize(history,canvas,deserializedData,map);
        }

        else {
            //TODO
            return null;
        }
    }


    @Override
    protected <T extends DesignerObject> T createInstance(String resolvedName, UUID uuid, DesignerObjectType type) {
        switch (type) {
            case IMAGE -> {
                return (T) new EditorImageObject(history,resolvedName, UUID.randomUUID(),canvas,new Point(0,0),new SignedDimension(0,0));
            }
            case COLLISION -> {
                return (T) new EditorCollisionObject(history,resolvedName,UUID.randomUUID(),canvas,new Point(0,0),new SignedDimension(0,0));
            }
            default -> {
                return null;
            }

        }
    }

    @Override
    public boolean removeInstance(DesignerObject object) {
        boolean result = super.removeInstance(object);
        if (result && saveHistory) {
            history.add(new EditRegisteredMaterialList(object.getUUID(),object.getName(),((EditorDesignerObject)object).serialize(),false));
        }
        return result;
    }


    private record EditRegisteredMaterialList(UUID uuid,String name,Map<String,Object> serialized,boolean add) implements HistoryStaff {

        @Override
        public String description() {
            if (add) {
                return "add material:"+name;
            } else {
                return "remove material:"+name;
            }
        }

        @Override
        public void undo(Canvas canvas) {
            DesignerObjectManager manager = canvas.getDesignerObjectManager();
            if (manager instanceof EditorDesignerObjectManager editorDesignerObjectManager) {
                editorDesignerObjectManager.saveHistory = false;
                System.out.println("undo in manager");
                if (add) {
                    editorDesignerObjectManager.removeInstance(editorDesignerObjectManager.getInstance(uuid));
                } else {
                    editorDesignerObjectManager.deserializeManagedObject(serialized);
                }
                editorDesignerObjectManager.saveHistory = true;
            }
        }

        @Override
        public void redo(Canvas canvas) {
            DesignerObjectManager manager = canvas.getDesignerObjectManager();
            if (manager instanceof EditorDesignerObjectManager editorDesignerObjectManager) {
                editorDesignerObjectManager.saveHistory = false;
                System.out.println("redo in manager");
                if (!add) {
                    editorDesignerObjectManager.removeInstance(editorDesignerObjectManager.getInstance(uuid));
                } else {
                    editorDesignerObjectManager.deserializeManagedObject(serialized);
                }
                editorDesignerObjectManager.saveHistory = true;
            }
        }
    }
}
