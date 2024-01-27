package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.Util;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.DesignerObjectType;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.UUID;

/**
 * 基本的なコリジョン表示用オブジェクトの実装
 */
public class SimpleCollisionObject extends SimpleDesignerObject {

    /**
     * 描画色
     */
    public static final Color color = new Color(180, 0, 0, 100);

    /**
     * コリジョン表示用オブジェクトのインスタンスを作成する
     *
     * @param name      このオブジェクトの名前
     * @param uuid      UUID
     * @param canvas    キャンバス
     * @param position  座標
     * @param z         z座標
     * @param dimension 表示領域
     * @param priority  優先度
     */
    public SimpleCollisionObject(String name, UUID uuid, Canvas canvas, Point position,double z, SignedDimension dimension, int priority) {
        super(name, uuid, DesignerObjectType.COLLISION, canvas, position,z, dimension, priority);
    }

    /**
     * コリジョン表示用オブジェクトのインスタンスを作成する
     *
     * @param name      このオブジェクトの名前
     * @param uuid      UUID
     * @param canvas    キャンバス
     * @param position  座標
     * @param dimension 表示領域
     */
    public SimpleCollisionObject(String name, UUID uuid, Canvas canvas, Point position, SignedDimension dimension) {
        super(name, uuid, DesignerObjectType.COLLISION, canvas, position, dimension);
    }


    @Override
    public MouseAdapter getMouseAdapter() {
        return null;
    }

    @Override
    public void draw(Graphics2D g) {
        Rectangle rectangle = Util.getRectangleOnScreen(getCanvas(), this);
        g.setColor(color);
        g.fill(rectangle);
    }
}
