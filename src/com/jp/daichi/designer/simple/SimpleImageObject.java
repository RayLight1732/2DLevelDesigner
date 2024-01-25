package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.Util;
import com.jp.daichi.designer.editor.ui.ViewUtil;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.UUID;

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
     * @param dimension    表示領域
     **/
    public SimpleImageObject(String name, UUID uuid, UUID materialUUID, Canvas canvas, Point position, SignedDimension dimension) {
        this(name, uuid, canvas, position, dimension);
        this.materialUUID = materialUUID;
    }

    @Override
    public MouseAdapter getMouseAdapter() {
        return null;//TODO
    }

    @Override
    public void draw(Graphics2D g) {
        Rectangle rectangle = Util.getRectangleOnScreen(getCanvas(), this);
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
                        rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height,
                        Util.round(uvX), Util.round(uvY), Util.round(uvX + uvWidth), Util.round(uvY + uvHeight), null);
            }
        } catch (NullPointerException exception) {
            drawMissing(g, rectangle);
        }
    }

    private void drawMissing(Graphics2D g, Rectangle rectangle) {
        g.setColor(ViewUtil.MATERIAL_ERROR_COLOR);
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
