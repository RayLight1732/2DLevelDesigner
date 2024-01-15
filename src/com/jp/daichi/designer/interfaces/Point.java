package com.jp.daichi.designer.interfaces;

import com.jp.daichi.designer.Utils;

import java.io.Serializable;

public record Point(double x, double y) implements Serializable {

    public static final Point ZERO = new Point(0, 0);

    public static Point convert(java.awt.Point point) {
        return new Point(point.x,point.y);
    }

    /**
     * 現在の点からある点を減算した点の新しいインスタンスを返す
     * @param point 対象の点
     * @return 新しいインスタンス
     */
    public Point subtract(Point point) {
        return new Point(x- point.x(),y-point.y());
    }

    /**
     * 現在の点にある点を加算した点の新しいインスタンスを返す
     * @param point 対象の点
     * @return 新しいインスタンス
     */
    public Point add(Point point) {
        return new Point(x+ point.x(),y+point.y());
    }

    public java.awt.Point convert() {
        return new java.awt.Point(Utils.round(x),Utils.round(y));
    }

}
