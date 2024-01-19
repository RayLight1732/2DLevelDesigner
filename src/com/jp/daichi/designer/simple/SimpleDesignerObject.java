package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.simple.editor.UpdateAction;

import java.awt.*;
import java.util.UUID;


/**
 * デザイナーオブジェクトの基本的な実装
 */
public abstract class SimpleDesignerObject extends SimpleObservedObject implements DesignerObject {

    private final Canvas canvas;
    private Point position;
    private SignedDimension dimension;
    private int priority = 0;
    private boolean isVisible = true;

    private double z = 0;
    private String name;
    private final UUID uuid;

    /**
     * デザイナーオブジェクトのインスタンスを作成する
     * @param name このオブジェクトの名前
     * @param canvas キャンバス
     * @param position 座標
     * @param dimension 表示領域
     */
    public SimpleDesignerObject(String name,UUID uuid, Canvas canvas, Point position, SignedDimension dimension) {
        this.name = name;
        this.uuid = uuid;
        this.canvas = canvas;
        this.position = position;
        this.dimension = dimension;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point point) {
        if (this.position == null || point == null || point.x() != this.position.x() || point.y() != this.position.y()) {
            this.position = point;
            sendUpdate(UpdateAction.CHANGE_RECTANGLE);
        }
    }

    @Override
    public SignedDimension getDimension() {
        return dimension;
    }

    @Override
    public void setDimension(SignedDimension dimension) {
        if (this.dimension == null || dimension == null || dimension.width() != this.dimension.width() || dimension.height() != this.dimension.height()) {
            this.dimension = dimension;
            sendUpdate(UpdateAction.CHANGE_RECTANGLE);
        }
    }


    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
        sendUpdate(UpdateAction.CHANGE_PRIORITY);
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
        sendUpdate(UpdateAction.CHANGE_VISIBLY);
    }

    @Override
    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public boolean isSelectable() {
        return isVisible();
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public void setZ(double z) {
        this.z = z;
        sendUpdate(UpdateAction.CHANGE_Z);
    }

    @Override
    public int compareTo(DesignerObject o) {
        double d = getZ()- o.getZ();
        if (d == 0) {
            return getPriority()-o.getPriority();
        } else {
            return (int) Math.signum(d);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        sendUpdate(UpdateAction.CHANGE_NAME);
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
}
