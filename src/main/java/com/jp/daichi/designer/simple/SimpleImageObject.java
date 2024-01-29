package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.Util;
import com.jp.daichi.designer.editor.ui.ViewUtil;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.geom.Rectangle2D;
import java.util.UUID;

import static com.jp.daichi.designer.ColorProfile.MATERIAL_ERROR_COLOR;

/**
 * 基本的なイメージオブジェクトの実装
 */
public class SimpleImageObject extends SimpleDesignerObject implements ImageObject {

    public static final String MATERIAL_UUID = "MaterialUUID";

    private UUID materialUUID;

    /**
     * イメージオブジェクトのインスタンスを作成する
     *
     * @param name      このオブジェクトの名前
     * @param uuid      UUID
     * @param canvas    キャンバス
     * @param position  座標
     * @param dimension 表示領域
     **/
    public SimpleImageObject(String name, UUID uuid, Canvas canvas, Point position, SignedDimension dimension) {
        super(name, uuid, DesignerObjectType.IMAGE, canvas, position, dimension);
    }

    /**
     * イメージオブジェクトのインスタンスを作成する
     *
     * @param name         このオブジェクトの名前
     * @param uuid         UUID
     * @param materialUUID マテリアルUUID
     * @param canvas       キャンバス
     * @param position     座標
     * @param z            z座標
     * @param dimension    表示領域
     * @param priority     優先度
     **/
    public SimpleImageObject(String name, UUID uuid, UUID materialUUID, Canvas canvas, Point position,double z, SignedDimension dimension, int priority) {
        super(name, uuid, DesignerObjectType.IMAGE, canvas, position,z, dimension, priority);
        this.materialUUID = materialUUID;
    }

    @Override
    public MouseAdapter getMouseAdapter() {
        return null;//TODO
    }

    @Override
    public void draw(Graphics2D g) {
        Rectangle2D rectangle = Util.getRectangleOnScreen(getCanvas(), this);
        if (getName().equals("tree(1)(1)(2)(1)") || getName().equals("tree(1)(2)(2)(1)")) {
            System.out.println(rectangle);
        }
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
                        ceil(rectangle.getX()),ceil(rectangle.getY()),ceil(rectangle.getX() + rectangle.getWidth()),ceil(rectangle.getY() + rectangle.getHeight()),
                        Util.round(uvX), Util.round(uvY), Util.round(uvX + uvWidth), Util.round(uvY + uvHeight), null);
            }
        } catch (NullPointerException exception) {
            drawMissing(g, rectangle);
        }
    }

    private int ceil(double d) {
        return (int) Math.ceil(d);
    }
    protected void drawMissing(Graphics2D g, Rectangle2D rectangle) {
        g.setColor(MATERIAL_ERROR_COLOR);
        g.fill(rectangle);
    }


    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
    }

    @Override
    public boolean isSelectable() {
        return isVisible();
    }


    @Override
    public void setMaterialUUID(UUID uuid) {
        this.materialUUID = uuid;
        sendUpdate(UpdateAction.CHANGE_MATERIAL);
    }

    public UUID getMaterialUUID() {
        return materialUUID;
    }
}
