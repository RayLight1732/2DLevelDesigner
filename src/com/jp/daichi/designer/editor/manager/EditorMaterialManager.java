package com.jp.daichi.designer.editor.manager;

import com.jp.daichi.designer.editor.EditorMaterial;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.interfaces.editor.HistoryStaff;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;
import com.jp.daichi.designer.simple.manager.SimpleMaterialManager;

import java.util.Map;
import java.util.UUID;

public class EditorMaterialManager extends SimpleMaterialManager {

    private final History history;
    private boolean saveHistory = true;

    public EditorMaterialManager(History history) {
        this.history = history;
    }

    @Override
    public Material deserializeManagedObject(Map<String, Object> map) {
        EditorMaterial material = EditorMaterial.deserialize(history,this,map);
        if (material != null) {
            addInstance(material);
        }
        return material;
    }
    @Override
    public boolean removeInstance(Material object) {
        boolean result = super.removeInstance(object);
        if (saveHistory) {
            history.add(new EditRegisteredMaterialList(object.getUUID(),object.getName(),((EditorMaterial)object).serialize(),false));
        }
        return result;
    }

    @Override
    public Material createInstance(String name) {
        EditorMaterial material = new EditorMaterial(history,resolveName(null,name),UUID.randomUUID(),this);
        addInstance(material);
        if (saveHistory) {
            history.add(new EditRegisteredMaterialList(material.getUUID(),material.getName(),material.serialize(),true));
        }
        return material;
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
            MaterialManager materialManager = canvas.getMaterialManager();
            if (materialManager instanceof EditorMaterialManager editorMaterialManager) {
                if (add) {
                    editorMaterialManager.saveHistory = false;
                    materialManager.removeInstance(materialManager.getInstance(uuid));
                    editorMaterialManager.saveHistory = true;
                } else {
                    editorMaterialManager.saveHistory = false;
                    materialManager.deserializeManagedObject(serialized);
                    editorMaterialManager.saveHistory = true;
                }
            }
        }

        @Override
        public void redo(Canvas canvas) {
            MaterialManager materialManager = canvas.getMaterialManager();
            if (materialManager instanceof EditorMaterialManager editorMaterialManager) {
                if (!add) {
                    editorMaterialManager.saveHistory = false;
                    materialManager.removeInstance(materialManager.getInstance(uuid));
                    editorMaterialManager.saveHistory = true;
                } else {
                    editorMaterialManager.saveHistory = false;
                    materialManager.deserializeManagedObject(serialized);
                    editorMaterialManager.saveHistory = true;
                }
            }
        }
    }
}
