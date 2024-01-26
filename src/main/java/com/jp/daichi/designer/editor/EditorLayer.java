package com.jp.daichi.designer.editor;

import com.jp.daichi.designer.editor.history.SimpleHistoryStaff;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.DesignerObjectType;
import com.jp.daichi.designer.interfaces.Layer;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.interfaces.editor.HistoryStaff;
import com.jp.daichi.designer.interfaces.editor.PermanentObject;
import com.jp.daichi.designer.simple.SimpleLayer;

import java.util.*;

/**
 * エディタ用のレイヤーの実装
 */
public class EditorLayer extends SimpleLayer implements PermanentObject {

    /**
     * デシリアライズを行う
     *
     * @param history 履歴
     * @param map     シリアライズされたデータ
     * @return デシリアライズの結果
     */
    public static Layer deserialize(History history, Map<String, Object> map) {
        try {
            String name = (String) map.get("Name");
            UUID uuid = (UUID) map.get("UUID");
            List<UUID> objects = (List<UUID>) map.get("Objects");
            DesignerObjectType type = (DesignerObjectType) map.get("Type");
            Objects.requireNonNull(name);
            Objects.requireNonNull(uuid);
            Objects.requireNonNull(objects);
            Objects.requireNonNull(type);
            EditorLayer result = new EditorLayer(history, name, uuid, type);
            Boolean isVisible = (Boolean) map.get("isVisible");
            if (isVisible != null) {
                result.setVisible(isVisible);
            }
            result.setSaveHistory(false);
            for (UUID designerObjectsUUID : objects) {
                result.add(designerObjectsUUID);
            }
            result.setSaveHistory(true);
            return result;
        } catch (NullPointerException | ClassCastException e) {
            return null;
        }
    }

    private final History history;
    private boolean saveHistory = true;

    /**
     * 新しいレイヤーのインスタンスを作成する
     *
     * @param history    履歴
     * @param name       名前
     * @param uuid       UUID
     * @param objectType このレイヤーが管理するデザイナーオブジェクトのタイプ
     */
    public EditorLayer(History history, String name, UUID uuid, DesignerObjectType objectType) {
        super(name, uuid, objectType);
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
            history.add(new EditRegisteredObjectList(getUUID(), designerObjectUUID, false));
        }
        return result;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("Name", getName());
        result.put("UUID", getUUID());
        result.put("Objects", getObjects());
        result.put("Type", getObjectType());
        result.put("isVisible", isVisible());
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

    private static class SetName extends SimpleHistoryStaff<EditorLayer, String> {

        public SetName(UUID uuid, String oldValue, String newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String description() {
            return "Change Name:" + newValue;
        }

        @Override
        public void setValue(EditorLayer target, String value) {
            target.setName(value);
        }

        @Override
        public EditorLayer getTarget(Canvas canvas) {
            return (EditorLayer) canvas.getLayerManager().getInstance(uuid);
        }
    }

    private record EditRegisteredObjectList(UUID uuid, UUID target, boolean add) implements HistoryStaff {

        @Override
        public String description() {
            if (add) {
                return "add object to layer";
            } else {
                return "remove object from layer";
            }
        }

        @Override
        public void undo(Canvas canvas) {
            EditorLayer layer = (EditorLayer) canvas.getLayerManager().getInstance(uuid);
            if (layer != null) {
                layer.setSaveHistory(false);
                System.out.println("undo");
                if (add) {
                    layer.remove(target);
                } else {
                    System.out.println("add:" + target.toString());
                    layer.add(target);
                }
                layer.setSaveHistory(true);
            }
        }

        @Override
        public void redo(Canvas canvas) {
            EditorLayer layer = (EditorLayer) canvas.getLayerManager().getInstance(uuid);
            if (layer != null) {
                layer.setSaveHistory(false);
                System.out.println("redo");
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
