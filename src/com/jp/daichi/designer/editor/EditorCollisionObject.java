package com.jp.daichi.designer.editor;

import com.jp.daichi.designer.editor.history.DesignerObjectHistoryStaff;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.DesignerObjectType;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.interfaces.editor.EditorDesignerObject;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.interfaces.editor.PermanentObject;
import com.jp.daichi.designer.simple.DesignerObjectSerializer;
import com.jp.daichi.designer.simple.SimpleCollisionObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * エディタ用のコリジョン表示用オブジェクトの実装
 */
public class EditorCollisionObject extends SimpleCollisionObject implements PermanentObject, EditorDesignerObject {

    /**
     * デシリアライズを行う
     * @param history 履歴
     * @param canvas  キャンバス
     * @param deserializedData デシリアライズされたデータ
     * @param serialized シリアライズされたデータ
     * @return デシリアライズされた結果
     */
    public static EditorCollisionObject deserialize(History history, Canvas canvas, DesignerObjectSerializer.DeserializedData deserializedData, Map<String,Object> serialized) {
        return new EditorCollisionObject(history, deserializedData.name(),deserializedData.uuid(),canvas,deserializedData.position(),deserializedData.dimension(),deserializedData.priority());

    }

    private final History history;
    private boolean saveHistory = true;

    /**
     * 子リジョン表示用オブジェクトのインスタンスを作成する
     *
     * @param history 履歴
     * @param name      このオブジェクトの名前
     * @param uuid      UUID
     * @param canvas    キャンバス
     * @param position  座標
     * @param dimension 表示領域
     * @param priority  優先度
     */
    public EditorCollisionObject(History history,String name, UUID uuid, Canvas canvas, Point position, SignedDimension dimension, int priority) {
        super(name, uuid, canvas, position, dimension, priority);
        this.history = history;
    }

    /**
     * コリジョン表示用オブジェクトのインスタンスを作成する
     * @param history 履歴
     * @param name このオブジェクトの名前
     * @param uuid UUID
     * @param canvas キャンバス
     * @param position 座標
     * @param dimension 表示領域
     */
    public EditorCollisionObject(History history,String name,UUID uuid,Canvas canvas, Point position, SignedDimension dimension) {
        super(name, uuid, canvas, position, dimension);
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
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        DesignerObjectSerializer.serialize(this,result);
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
}
