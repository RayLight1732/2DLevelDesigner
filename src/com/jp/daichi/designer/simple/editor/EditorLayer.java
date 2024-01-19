package com.jp.daichi.designer.simple.editor;

import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.Layer;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.interfaces.editor.HistoryStaff;
import com.jp.daichi.designer.interfaces.editor.PermanentObject;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.interfaces.manager.LayerManager;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;
import com.jp.daichi.designer.simple.SimpleLayer;
import com.jp.daichi.designer.simple.editor.history.SimpleHistoryStaff;

import java.util.*;

public class EditorLayer extends SimpleLayer implements PermanentObject {

    public static Layer deserialize(History history,Map<String,Object> map) {
        try {
            String name = (String) map.get("Name");
            UUID uuid = (UUID) map.get("UUID");
            List<UUID> objects = (List<UUID>)map.get("Objects");
            Objects.requireNonNull(name);
            Objects.requireNonNull(uuid);
            Objects.requireNonNull(objects);
            EditorLayer result = new EditorLayer(history,name,uuid);
            result.setSaveHistory(false);
            for (UUID designerObjectsUUID:objects) {
                result.add(designerObjectsUUID);
            }
            result.setSaveHistory(true);
            return result;
        } catch (NullPointerException|ClassCastException e) {
            return null;
        }
    }

    private final History history;
    private boolean saveHistory = true;

    public EditorLayer(History history, String name, UUID uuid) {
        super(name, uuid);
        this.history = history;
    }

    @Override
    public void setName(String name) {
        String oldValue = getName();
        super.setName(name);
        if (saveHistory) {
            history.add(new SetName(getUUID(), oldValue, getName()));
        }
    }

    @Override
    public void add(UUID designerObjectUUID) {
        super.add(designerObjectUUID);
        if (saveHistory) {
            history.add(new EditRegisteredObjectList(getUUID(), designerObjectUUID, true));
        }
    }

    @Override
    public boolean remove(UUID designerObjectUUID) {
        boolean result = super.remove(designerObjectUUID);
        if (result && saveHistory) {
            history.add(new EditRegisteredObjectList(getUUID(), designerObjectUUID, true));
        }
        return result;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String,Object> result = new HashMap<>();
        result.put("Name",getName());
        result.put("UUID",getUUID());
        result.put("Objects",getObjects());
        return result;
    }

    @Override
    public boolean saveHistory() {
        return saveHistory;
    }

    @Override
    public void setSaveHistory(boolean saveHistory) {
        this.saveHistory = saveHistory;
    }

    private static class SetName extends SimpleHistoryStaff<EditorLayer,String> {

        public SetName(UUID uuid, String oldValue, String newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String getDescription() {
            return "Change Name:"+newValue;
        }

        @Override
        public void setValue(EditorLayer target, String value) {
            target.setName(value);
        }

        @Override
        public EditorLayer getTarget(DesignerObjectManager designerObjectManager, LayerManager layerManager, MaterialManager materialManager) {
            return (EditorLayer) layerManager.getInstance(uuid);
        }
    }

    private static class EditRegisteredObjectList implements HistoryStaff {

        private final UUID uuid;
        private final UUID target;
        private final boolean add;

        private EditRegisteredObjectList(UUID uuid,UUID target,boolean add) {
            this.uuid = uuid;
            this.target = target;
            this.add = add;
        }

        @Override
        public String getDescription() {
            if (add) {
                return "add object to layer";
            } else {
                return "remove object from layer";
            }
        }

        @Override
        public void undo(DesignerObjectManager designerObjectManager, LayerManager layerManager, MaterialManager materialManager) {
            EditorLayer layer = (EditorLayer) layerManager.getInstance(uuid);
            if (layer != null) {
                layer.setSaveHistory(false);
                if (add) {
                    layer.remove(target);
                } else {
                    layer.add(target);
                }
                layer.setSaveHistory(true);
            }
        }

        @Override
        public void redo(DesignerObjectManager designerObjectManager, LayerManager layerManager, MaterialManager materialManager) {
            EditorLayer layer = (EditorLayer) layerManager.getInstance(uuid);
            if (layer != null) {
                layer.setSaveHistory(false);
                if (add) {
                    layer.add(target);
                } else {
                    layer.remove(target);
                }
                layer.setSaveHistory(true);
            }
        }
    }

}
