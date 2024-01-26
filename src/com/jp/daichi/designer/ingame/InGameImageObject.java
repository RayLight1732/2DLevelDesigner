package com.jp.daichi.designer.ingame;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.simple.DesignerObjectSerializer;
import com.jp.daichi.designer.simple.SimpleImageObject;

import java.util.Map;
import java.util.UUID;

/**
 * ゲーム用のイメージオブジェクトの実装
 */
public class InGameImageObject extends SimpleImageObject {

    /**
     * デシリアライズを行う
     *
     * @param canvas           キャンバス
     * @param deserializedData デシリアライズされたデータ
     * @param serialized       シリアライズされたデータ
     * @return デシリアライズの結果
     */
    public static InGameImageObject deserialize(Canvas canvas, DesignerObjectSerializer.DeserializedData deserializedData, Map<String, Object> serialized) {
        try {
            if (deserializedData != null) {
                UUID materialUUID = (UUID) serialized.get(MATERIAL_UUID);
                return new InGameImageObject(deserializedData.name(), deserializedData.uuid(), materialUUID, canvas, deserializedData.position(), deserializedData.dimension(), deserializedData.priority());
            } else {
                return null;
            }
        } catch (NullPointerException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * イメージオブジェクトのインスタンスを作成する
     *
     * @param name      このオブジェクトの名前
     * @param uuid      UUID
     * @param canvas    キャンバス
     * @param position  座標
     * @param dimension 表示領域
     **/
    public InGameImageObject(String name, UUID uuid, Canvas canvas, Point position, SignedDimension dimension) {
        super(name, uuid, canvas, position, dimension);
    }

    /**
     * イメージオブジェクトのインスタンスを作成する
     *
     * @param name         このオブジェクトの名前
     * @param uuid         UUID
     * @param materialUUID マテリアルUUID
     * @param canvas       キャンバス
     * @param position     座標
     * @param dimension    表示領域
     * @param priority     優先度
     **/
    public InGameImageObject(String name, UUID uuid, UUID materialUUID, Canvas canvas, Point position, SignedDimension dimension, int priority) {
        super(name, uuid, materialUUID, canvas, position, dimension, priority);
    }

}
