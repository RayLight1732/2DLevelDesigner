package com.jp.daichi.designer.ingame.manager;

import com.jp.daichi.designer.ingame.InGameImageObject;
import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.simple.DesignerObjectSerializer;
import com.jp.daichi.designer.simple.manager.SimpleDesignerObjectManager;

import java.util.Map;
import java.util.UUID;

/**
 * ゲーム用のデザイナーオブジェクトマネージャーの実装
 */
public class InGameDesignerObjectManager extends SimpleDesignerObjectManager {

    private Canvas canvas;

    /**
     * 対象となるキャンバスを設定
     *
     * @param canvas キャンバス
     */
    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T extends DesignerObject> T createInstance(String resolvedName, UUID uuid, DesignerObjectType type) {
        if (type == DesignerObjectType.IMAGE) {
            return (T) new InGameImageObject(resolvedName, UUID.randomUUID(), canvas, new Point(0, 0), new SignedDimension(0, 0));
        } else {
            //TODO その他のタイプのインスタンス化
            return null;
        }
    }


    @Override
    protected DesignerObject deserializeManagedObject(DesignerObjectSerializer.DeserializedData deserializedData, Map<String, Object> map) {
        if (deserializedData.type() == DesignerObjectType.IMAGE) {
            return InGameImageObject.deserialize(canvas, deserializedData, map);
        } else {
            //TODO
            return null;
        }
    }
}
