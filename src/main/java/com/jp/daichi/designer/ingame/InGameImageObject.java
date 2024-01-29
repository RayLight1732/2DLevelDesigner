package com.jp.daichi.designer.ingame;

import com.jp.daichi.designer.Util;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.simple.DesignerObjectSerializer;
import com.jp.daichi.designer.simple.SimpleImageObject;

import java.awt.*;
import java.awt.geom.Rectangle2D;
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
                return new InGameImageObject(deserializedData.name(), deserializedData.uuid(), materialUUID, canvas, deserializedData.position(), deserializedData.z(), deserializedData.dimension(), deserializedData.priority());
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
    public InGameImageObject(String name, UUID uuid, UUID materialUUID, Canvas canvas, Point position, double z,SignedDimension dimension, int priority) {
        super(name, uuid, materialUUID, canvas, position,z, dimension, priority);
    }

    @Override
    public void draw(Graphics2D g) {
        Rectangle2D rectangle = Util.getRectangleOnScreen(getCanvas(), this);
        try {
            Material material = getCanvas().getMaterialManager().getInstance(getMaterialUUID());
            if (material == null || material.getImage() == null) {
                drawMissing(g, rectangle);
            } else {
                double uvX = material.getUV().x();
                double uvY = material.getUV().y();
                double uvWidth = material.getUVDimension().width();
                double uvHeight = material.getUVDimension().height();
                g.drawImage(material.getImage(),
                        ceil(rectangle.getX()),ceil( rectangle.getY()),ceil( rectangle.getX() + rectangle.getWidth()),ceil( rectangle.getY() + rectangle.getHeight()),
                        Util.round(uvX), Util.round(uvY), Util.round(uvX + uvWidth), Util.round(uvY + uvHeight), null);
            }
        } catch (NullPointerException exception) {
            drawMissing(g, rectangle);
        }
    }

    private int ceil(double d) {
        return (int) Math.ceil(d);
    }

}
