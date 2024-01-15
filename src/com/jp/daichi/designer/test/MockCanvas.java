package com.jp.daichi.designer.test;

import com.jp.daichi.designer.Utils;
import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Frame;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.simple.SimpleFrame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;

/**
 * テスト用キャンバス
 */
public class MockCanvas implements Canvas {

    private final Frame frame = new SimpleFrame(this);
    private final List<Layer> layers = new ArrayList<>();

    @Override
    public List<Layer> getLayers() {
        return new ArrayList<>(layers);
    }

    @Override
    public boolean removeLayer(Layer layer) {
        return layers.remove(layer);
    }

    @Override
    public boolean addLayer(Layer layer) {
        boolean result = layers.add(layer);
        if (result) {
            layer.setUpdateObserver(frame.getUpdateObserver());
        }
        return result;
    }

    @Override
    public Frame getFrame() {
        return frame;
    }

    @Override
    public DesignerObject getDesignerObject(Point point) {
        for (Layer layer:layers) {
            if (layer.isVisible()) {
                for (DesignerObject designerObject : layer.getObjects()) {
                    if (designerObject.isVisible() && Utils.getRectangleOnScreen(this,designerObject).contains(point.convert())) {
                        return designerObject;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Set<DesignerObject> getDesignerObjects(Rectangle rectangle) {
        Set<DesignerObject> result = new HashSet<>();
        for (Layer layer:layers) {
            if (layer.isVisible()) {
                for (DesignerObject designerObject:layer.getObjects()) {
                    if (designerObject.isVisible() && Utils.getRectangleOnScreen(this,designerObject).intersects(rectangle)) {
                        result.add(designerObject);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void draw(Graphics2D g,int width,int height) {
        AffineTransform transform = g.getTransform();
        transform.scale((double) getViewPort().width/width,(double) getViewPort().height/height);
        g.setTransform(transform);
        for (Layer layer:layers) {
            if (layer.isVisible()) {
                layer.draw(g);
            }
        }
        frame.draw(g);
    }

    @Override
    public void setUpdateObserver(UpdateObserver updateObserver) {
        for (Layer layer:layers) {
            layer.setUpdateObserver(updateObserver);
        }
        frame.setUpdateObserver(updateObserver);
    }

    private Rectangle rectangle = new Rectangle(0,0,1920,1080);

    @Override
    public void setViewPort(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    public Rectangle getViewPort() {
        return rectangle;
    }

    private double pov = Math.toRadians(60);

    @Override
    public void setPov(double angle) {
        this.pov = angle;
    }

    @Override
    public double getPov() {
        return pov;
    }

    @Override
    public Point convertToScreenPosition(Point point, double z) {
        double distance = rectangle.width/2.0/Math.tan(pov/2);//カメラからスクリーンまでの距離
        double centerX = rectangle.x+rectangle.width/2.0;
        double centerY = rectangle.y+rectangle.height/2.0;
        double x = distance/(distance+z)*(point.x()-centerX)+rectangle.width/2.0;
        double y = distance/(distance+z)*(point.y()-centerY)+rectangle.height/2.0;
        return new Point(x,y);
    }

    @Override
    public Point convertFromScreenPosition(Point point,double z) {
        double distance = rectangle.width/2.0/Math.tan(pov/2);//カメラからスクリーンまでの距離
        double centerX = rectangle.x+rectangle.width/2.0;
        double centerY = rectangle.y+rectangle.height/2.0;
        double x = (distance+z)*(point.x()-rectangle.width/2.0)/distance+centerX;
        double y = (distance+z)*(point.y()-rectangle.height/2.0)/distance+centerY;
        return new Point(x,y);
    }
}
