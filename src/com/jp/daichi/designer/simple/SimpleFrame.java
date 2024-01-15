package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.Utils;
import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Frame;
import com.jp.daichi.designer.interfaces.Point;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.*;

public class SimpleFrame extends SimpleDesignerObject implements Frame {

    private final Set<DesignerObject> selectedObjects = new HashSet<>();

    /**
     * フレームのインスタンスを作成する
     * @param canvas キャンバス
     */
    public SimpleFrame(Canvas canvas) {
        super("Frame",canvas,null,null);
    }

    @Override
    public MouseAdapter getMouseAdapter() {
        return null;
    }

    @Override
    public void draw(Graphics2D g) {
        if (getSelectedObjectCount() != 0) {
            //updateRectangle();
            Utils.drawSelectedFrame(g,this);
        }
    }

    @Override
    public Set<DesignerObject> getSelected() {
        return new HashSet<>(selectedObjects);
    }

    @Override
    public boolean addSelectedObject(DesignerObject designerObject) {
        if (getSelectedObjectCount() == 0) {
            selectedObjects.add(designerObject);
            setZ(designerObject.getZ());
            updateRectangle();
            return true;
        } else if (designerObject.getZ() == getZ()) {
            boolean result = selectedObjects.add(designerObject);
            updateRectangle();
            return result;
        } else {
            return false;
        }
    }

    @Override
    public void addAll(Collection<DesignerObject> designerObjects) {
        for (DesignerObject designerObject:designerObjects) {
            addSelectedObject(designerObject);
        }
        updateRectangle();
    }

    @Override
    public boolean removeSelectedObject(DesignerObject designerObject) {
        boolean result = selectedObjects.remove(designerObject);
        updateRectangle();
        return result;
    }

    @Override
    public void clearSelectedObject() {
        selectedObjects.clear();
        updateRectangle();
    }

    @Override
    public int getSelectedObjectCount() {
        return selectedObjects.size();
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public void setPosition(Point point) {
        Point now = getPosition();
        for (DesignerObject designerObject:selectedObjects) {
            designerObject.setPosition(point.add(designerObject.getPosition().subtract(now)));
        }
        super.setPosition(point);
    }

    @Override
    public void setPositionAndDimension(Point point,SignedDimension dimension) {
        double width = dimension.width();
        double height = dimension.height();
        for (DesignerObject designerObject: selectedObjects) {
            Point vec = designerObject.getPosition().subtract(getPosition());
            designerObject.setPosition(new Point(point.x()+vec.x()* width/getDimension().width(),point.y()+vec.y()*height/getDimension().height()));
            double widthRatio = designerObject.getDimension().width()/getDimension().width();
            double heightRatio = designerObject.getDimension().height()/getDimension().height();
            System.out.println(widthRatio+","+heightRatio);
            designerObject.setDimension(new SignedDimension(widthRatio*width,heightRatio*height));
        }
        super.setPosition(point);
        setDimension(new SignedDimension(width,height));
    }

    private void updateRectangle() {
        if (getSelectedObjectCount() == 0) {
            setDimension(null);
            super.setPosition(null);
        } else {
            double maxX = -Double.MAX_VALUE;
            double maxY = -Double.MAX_VALUE;
            double minX = Double.MAX_VALUE;
            double minY = Double.MAX_VALUE;
            for (DesignerObject designerObject : selectedObjects) {
                double x1 = designerObject.getPosition().x();
                double y1 = designerObject.getPosition().y();
                double x2 = x1+designerObject.getDimension().width();
                double y2 = y1+designerObject.getDimension().height();
                maxX = Math.max(maxX, Math.max(x1,x2));
                maxY = Math.max(maxY, Math.max(y1,y2));
                minX = Math.min(minX, Math.min(x1,x2));
                minY = Math.min(minY, Math.min(y1,y2));
            }
            super.setPosition(new Point(minX,minY));
            setDimension(new SignedDimension(maxX - minX, maxY - minY));
        }
    }

}
