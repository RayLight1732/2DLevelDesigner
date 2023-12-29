package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.interfaces.ImageObject;
import com.jp.daichi.designer.interfaces.Point;

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
     * @param point 座標
     * @param dimension 表示領域(コピーされて使用される)
     * @param image 画像
     */
    public SimpleImageObject(Point point,Dimension dimension,BufferedImage image) {
        this(point,new Dimension(dimension),image,Point.ZERO,new Dimension(image.getWidth(),image.getHeight()));
    }

    /**
     * イメージオブジェクトのインスタンスを作成する
     * @param point 座標
     * @param dimension 表示領域(コピーされて使用される)
     * @param image 画像
     * @param uvPoint UV座標
     * @param uvDimension 表示領域(コピーされて使用される) dimensionと値が異なる場合、縦横比を維持しないで最大まで拡大、縮小される
     */
    public SimpleImageObject(Point point, Dimension dimension,BufferedImage image,Point uvPoint,Dimension uvDimension) {
        super(point, dimension);
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
    }

    @Override
    public Dimension getUVDimension() {
        return new Dimension(uvDimension);
    }

    @Override
    public void setUVDimension(Dimension dimension) {
        this.uvDimension = new Dimension(dimension);
    }

    @Override
    public boolean isSelectable() {
        return isVisible();
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public void onDragged(Point to) {
        setPosition(to);
    }
}
