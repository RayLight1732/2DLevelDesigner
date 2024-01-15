package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.simple.editor.UpdateAction;

import java.awt.*;


/**
 * デザイナーオブジェクトの基本的な実装
 */
public abstract class SimpleDesignerObject implements DesignerObject {

    private final Canvas canvas;
    private Point center;
    private SignedDimension dimension;
    private int priority = 0;
    private boolean isVisible = true;
    private UpdateObserver observer;

    private double z = 0;
    private String name;

    /**
     * デザイナーオブジェクトのインスタンスを作成する
     * @param name このオブジェクトの名前
     * @param canvas キャンバス
     * @param center 中心座標
     * @param dimension 表示領域
     */
    public SimpleDesignerObject(String name,Canvas canvas, Point center, SignedDimension dimension) {
        this.name = name;
        this.canvas = canvas;
        this.center = center;
        this.dimension = dimension;
    }

    @Override
    public Point getPosition() {
        return center;
    }

    @Override
    public void setPosition(Point point) {
        if (this.center == null || point == null || point.x() != this.center.x() || point.y() != this.center.y()) {
            this.center = point;
            getUpdateObserver().update(this, UpdateAction.CHANGE_RECTANGLE);
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
            getUpdateObserver().update(this,UpdateAction.CHANGE_RECTANGLE);
        }
    }


    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
        getUpdateObserver().update(this,UpdateAction.CHANGE_PRIORITY);
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
        getUpdateObserver().update(this,UpdateAction.CHANGE_VISIBLY);
    }

    @Override
    public UpdateObserver getUpdateObserver() {
        return observer;
    }

    @Override
    public void setUpdateObserver(UpdateObserver observer) {
        this.observer = observer;
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
        getUpdateObserver().update(this,UpdateAction.CHANGE_Z);
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
        getUpdateObserver().update(this,UpdateAction.CHANGE_NAME);
    }
}
