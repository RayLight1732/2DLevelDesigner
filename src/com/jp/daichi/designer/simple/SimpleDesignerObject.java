package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.Point;

import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * デザイナーオブジェクトの基本的な実装
 */
public abstract class SimpleDesignerObject implements DesignerObject {

    private Point point;
    private Dimension dimension;
    private int priority = 0;
    private boolean isVisible = true;

    /**
     * デザイナーオブジェクトのインスタンスを作成する
     * @param point 座標
     * @param dimension 表示領域(コピーされて使用される)
     */
    public SimpleDesignerObject(Point point,Dimension dimension) {
        this.point = point;
        this.dimension = new Dimension(dimension);
    }

    @Override
    public Point getPosition() {
        return point;
    }

    @Override
    public void setPosition(Point point) {
        this.point = point;
    }

    @Override
    public Dimension getDimension() {
        return new Dimension(dimension);
    }

    @Override
    public void setDimension(Dimension dimension) {
        this.dimension = new Dimension(dimension);
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

}
