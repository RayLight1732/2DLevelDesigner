package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.Utils;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.ImageObject;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.simple.editor.UpdateAction;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;

/**
 * 基本的なイメージオブジェクトの実装
 */
public class SimpleImageObject extends SimpleDesignerObject implements ImageObject {

    private final BufferedImage image;
    private Point uvPoint;
    private Dimension uvDimension;
    private boolean isSelected = false;

    /**
     * イメージオブジェクトのインスタンスを作成する
     * @param name このオブジェクトの名前
     * @param canvas キャンバス
     * @param center 中心座標
     * @param dimension 表示領域
     * @param image 画像
     */
    public SimpleImageObject(String name,Canvas canvas,Point center,SignedDimension dimension,BufferedImage image) {
        this(name,canvas,center,dimension,image,Point.ZERO,new Dimension(image.getWidth(),image.getHeight()));
    }

    /**
     * イメージオブジェクトのインスタンスを作成する
     * @param name このオブジェクトの名前
     * @param canvas キャンバス
     * @param center 中心座標
     * @param dimension 表示領域
     * @param image 画像
     * @param uvPoint UV座標
     * @param uvDimension 表示領域(コピーされて使用される) dimensionと値が異なる場合、縦横比を維持しないで最大まで拡大、縮小される
     */
    public SimpleImageObject(String name,Canvas canvas, Point center, SignedDimension dimension, BufferedImage image, Point uvPoint, Dimension uvDimension) {
        super(name,canvas,center, dimension);
        this.image = image;
        this.uvPoint = uvPoint;
        this.uvDimension = new Dimension(uvDimension);
    }

    @Override
    public MouseAdapter getMouseAdapter() {
        return null;//TODO
    }

    @Override
    public void draw(Graphics2D g) {
        double x = getPosition().x();
        double y = getPosition().y();
        double w = getDimension().width();
        double h = getDimension().height();
        g.drawImage(image,
                Utils.round(x),Utils.round(y),Utils.round(x+w),Utils.round(y+h),
                Utils.round(uvPoint.x()),Utils.round(uvPoint.y()),Utils.round(uvPoint.x()+uvDimension.width), Utils.round(uvPoint.y()+uvDimension.height),null);
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public Point getUV() {
        return uvPoint;
    }

    @Override
    public void setUV(Point point) {
        this.uvPoint = point;
        getUpdateObserver().update(this, UpdateAction.CHANGE_UV);
    }

    @Override
    public Dimension getUVDimension() {
        return new Dimension(uvDimension);
    }

    @Override
    public void setUVDimension(Dimension dimension) {
        this.uvDimension = new Dimension(dimension);
        getUpdateObserver().update(this,UpdateAction.CHANGE_UV);
    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
    }

    @Override
    public boolean isSelectable() {
        return isVisible();
    }


}
