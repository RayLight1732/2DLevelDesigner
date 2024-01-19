package com.jp.daichi.designer.simple.editor;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.ImageObject;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.interfaces.editor.HistoryStaff;
import com.jp.daichi.designer.interfaces.editor.PermanentObject;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.interfaces.manager.LayerManager;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;
import com.jp.daichi.designer.simple.SimpleImageObject;
import com.jp.daichi.designer.simple.editor.history.DesignerObjectHistoryStaff;
import com.jp.daichi.designer.simple.editor.history.SimpleHistoryStaff;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * エディタ用のイメージオブジェクト
 */
public class EditorImageObject extends SimpleImageObject implements PermanentObject {

    private static final String MATERIAL_UUID = "MaterialUUID";

    public static EditorImageObject deserialize(History history,Canvas canvas,Map<String,Object> serialized) {
        try {
            DesignerObjectSerializer.DeserializedData deserializedData = DesignerObjectSerializer.deserialize(serialized);
            UUID materialUUID = (UUID) serialized.get(MATERIAL_UUID);
            return new EditorImageObject(history, deserializedData.name(),deserializedData.uuid(),canvas,deserializedData.position(),deserializedData.dimension());
        } catch (NullPointerException | ClassCastException e) {
            return null;
        }
    }

    private final History history;
    private boolean saveHistory = false;

    /**
     * イメージオブジェクトのインスタンスを作成する
     *
     * @param history   履歴
     * @param name      このオブジェクトの名前
     * @param uuid      UUID
     * @param canvas    キャンバス
     * @param position    座標
     * @param dimension 表示領域
     **/
    public EditorImageObject(History history, String name,UUID uuid, Canvas canvas, Point position, SignedDimension dimension) {
        super(name, uuid,canvas,position, dimension);
        this.history = history;
    }

    @Override
    public void setPosition(Point point) {
        Point oldValue = getPosition();
        super.setPosition(point);
        if (saveHistory) {
            history.add(DesignerObjectHistoryStaff.createPositionInstance(getUUID(), oldValue, getPosition()));
        }
    }

    @Override
    public void setDimension(SignedDimension dimension) {
        SignedDimension oldValue = getDimension();
        super.setDimension(dimension);
        if (saveHistory) {
            history.add(DesignerObjectHistoryStaff.createDimensionInstance(getUUID(), oldValue, getDimension()));
        }
    }

    @Override
    public void setPriority(int priority) {
        int oldValue = getPriority();
        super.setPriority(priority);
        if (saveHistory) {
            history.add(DesignerObjectHistoryStaff.createPriorityInstance(getUUID(), oldValue, getPriority()));
        }
    }

    @Override
    public void setZ(double z) {
        double oldValue = getZ();
        super.setZ(z);
        if (saveHistory) {
            history.add(DesignerObjectHistoryStaff.createZInstance(getUUID(), oldValue, getZ()));
        }
    }

    @Override
    public void setName(String name) {
        String oldValue = getName();
        super.setName(name);
        if (saveHistory) {
            history.add(DesignerObjectHistoryStaff.createNameInstance(getUUID(), oldValue, getName()));
        }
    }

    @Override
    public void setMaterialUUID(UUID uuid) {
        UUID oldValue = getUUID();
        super.setMaterialUUID(uuid);
        if (saveHistory) {
            history.add(new SetMaterialUUID(getUUID(), oldValue, getMaterialUUID()));
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        DesignerObjectSerializer.serialize(this,result);
        result.put(MATERIAL_UUID,getMaterialUUID());
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

    private static class SetMaterialUUID extends SimpleHistoryStaff<EditorImageObject,UUID> {

        public SetMaterialUUID(UUID uuid,UUID oldValue, UUID newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String getDescription() {
            return "Set Material";//TODO マテリアルの名前
        }

        @Override
        public void setValue(EditorImageObject target, UUID value) {
            target.setMaterialUUID(value);
        }

        @Override
        public EditorImageObject getTarget(DesignerObjectManager designerObjectManager, LayerManager layerManager, MaterialManager materialManager) {
            return (EditorImageObject) designerObjectManager.getInstance(uuid);
        }
    }
}
