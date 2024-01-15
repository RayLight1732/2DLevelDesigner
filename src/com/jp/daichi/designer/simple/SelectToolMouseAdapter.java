package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.KeyManager;
import com.jp.daichi.designer.Utils;
import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 選択、移動ツールのマウスアダプター
 */
public class SelectToolMouseAdapter extends MouseAdapter {

    private final SelectAndMoveTool tool;
    private final Canvas canvas;
    public SelectToolMouseAdapter(SelectAndMoveTool tool,Canvas canvas) {
        this.tool = tool;
        this.canvas = canvas;
    }

    private Direction direction = Direction.NONE;
    private Point firstMousePosition;
    private Point firstFramePosition;
    private SignedDimension firstDimension;
    private boolean selected = false;//何かしらのオブジェクトが選択されたとき

    @Override
    public void mousePressed(MouseEvent e) {
        firstMousePosition = Point.convert(e.getPoint());
        firstFramePosition = canvas.getFrame().getPosition();
        firstDimension = canvas.getFrame().getDimension();
        if (e.getButton() == MouseEvent.BUTTON1) {//右クリック
            direction = Utils.getSizeChangeAnchor(canvas.getFrame(), firstMousePosition);
            if (direction == Direction.NONE) {//どこのアンカーも指していない時
                DesignerObject designerObject = canvas.getDesignerObject(Point.convert(e.getPoint()));
                if (designerObject != null) {
                    if (!KeyManager.isPressed(KeyEvent.VK_SHIFT)) {//Shiftが押されているないとき
                        canvas.getFrame().clearSelectedObject();
                    }
                    if (canvas.getFrame().getZ() != designerObject.getZ()) {
                        canvas.getFrame().clearSelectedObject();
                    }
                    selected = canvas.getFrame().addSelectedObject(designerObject);
                } else {
                    canvas.getFrame().clearSelectedObject();
                }
            } else {
                selected = true;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point point = Point.convert(e.getPoint());
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (selected) {
                Point converted = canvas.convertFromScreenPosition(point,canvas.getFrame().getZ());
                Point pressPointConverted = canvas.convertFromScreenPosition(firstMousePosition,canvas.getFrame().getZ());
                double deltaX = converted.x()-pressPointConverted.x();
                double deltaY = converted.y()-pressPointConverted.y();

                if (direction == Direction.NONE || direction == Direction.CENTER) {
                    Point old = canvas.getFrame().getPosition();
                    canvas.getFrame().setPosition(new Point(old.x()+deltaX,old.y()+deltaY));
                    firstMousePosition = point;
                    firstFramePosition = canvas.getFrame().getPosition();
                } else {
                    double x = firstFramePosition.x();
                    double y = firstFramePosition.y();
                    double w = firstDimension.width();
                    double h = firstDimension.height();

                    Point northEast = null;
                    Point southWest = null;

                    switch (direction) {
                        case NORTH ->{
                            northEast = calculateNorthEast(x,y,w,h,0,deltaY);
                            southWest = calculateSouthWest(x,y,w,h);
                        }
                        case NORTH_EAST -> {
                            northEast = calculateNorthEast(x,y,w,h,deltaX,deltaY);
                            southWest = calculateSouthWest(x,y,w,h);
                        }
                        case EAST -> {
                            northEast = calculateNorthEast(x,y,w,h,deltaX,0);
                            southWest = calculateSouthWest(x,y,w,h);
                        }
                        case SOUTH_EAST -> {
                            northEast = calculateNorthEast(x,y,w,h,deltaX,0);
                            southWest = calculateSouthWest(x,y,w,h,0,deltaY);
                        }
                        case SOUTH -> {
                            northEast = calculateNorthEast(x,y,w,h);
                            southWest = calculateSouthWest(x,y,w,h,0,deltaY);
                        }
                        case SOUTH_WEST -> {
                            northEast = calculateNorthEast(x,y,w,h);
                            southWest = calculateSouthWest(x,y,w,h,deltaX,deltaY);
                        }
                        case WEST -> {
                            northEast = calculateNorthEast(x,y,w,h);
                            southWest = calculateSouthWest(x,y,w,h,deltaX,0);
                        }
                        case NORTH_WEST -> {
                            northEast = calculateNorthEast(x,y,w,h,0,deltaY);
                            southWest = calculateSouthWest(x,y,w,h,deltaX,0);
                        }

                    }
                    if (northEast != null && southWest != null) {
                        Point newPoint = new Point(northEast.x(),northEast.y());
                        SignedDimension newDimension = new SignedDimension(southWest.x()-northEast.x(),southWest.y()-northEast.y());
                        canvas.getFrame().setPositionAndDimension(newPoint,newDimension);
                    }
                }
            } else {
                tool.setRectangle(createRectangle(firstMousePosition,point));
            }
        }
    }

    private static final int limit = 3;
    private Point calculateNorthEast(double x,double y,double w,double h) {
        return calculateNorthEast(x, y, w, h,0,0);
    }
    //通常の計算を行った後、deltaX,deltaYだけ平行移動させた点を返す
    private Point calculateNorthEast(double x,double y,double w,double h,double deltaX,double deltaY) {
        if (w-deltaX < limit) {
            deltaX = w-limit;
        }
        if (h-deltaY < limit) {
            deltaY = h-limit;
        }
        return new Point(x+deltaX,y+deltaY);
    }
    private Point calculateSouthWest(double x,double y,double w,double h) {
        return calculateSouthWest(x, y, w, h,0,0);
    }

    //通常の計算を行った後、deltaX,deltaYだけ平行移動させた点を返す
    private Point calculateSouthWest(double x,double y,double w,double h,double deltaX,double deltaY) {
        if (w+deltaX < limit) {
            deltaX = limit-w;
        }
        if (h+deltaY < limit) {
            deltaY = limit-h;
        }
        return new Point(x+w+deltaX,y+h+deltaY);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (selected) {

            } else {
                canvas.getFrame().clearSelectedObject();
                canvas.getFrame().addAll(canvas.getDesignerObjects(createRectangle(firstMousePosition,Point.convert(e.getPoint()))));
            }
            tool.setRectangle(null);
            selected =false;
            direction = Direction.NONE;
            firstMousePosition = null;
        }
    }

    private Rectangle createRectangle(Point p1,Point p2) {
        double x = Math.min(p1.x(),p2.x());
        double y = Math.min(p1.y(),p2.y());
        double w = Math.abs(p1.x()-p2.x());
        double h = Math.abs(p1.y()-p2.y());
        return new Rectangle(Utils.round(x),Utils.round(y),Utils.round(w),Utils.round(h));
    }
}
