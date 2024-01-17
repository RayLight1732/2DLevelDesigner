package com.jp.daichi.designer.test;

import com.jp.daichi.designer.Utils;
import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Frame;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.simple.SimpleFrame;
import com.jp.daichi.designer.simple.editor.ViewUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

/**
 * テスト用キャンバス
 */
public class MockCanvas  implements Canvas {

    private final Frame frame = new SimpleFrame(this);
    private final List<Layer> layers = new ArrayList<>();
    private final MaterialManager materialManager;

    public MockCanvas(MaterialManager materialManager) {
        this.materialManager = materialManager;
    }

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

    private int drawX = 0;
    private int drawY = 0;
    private double scale = 1;
    //画面上の座標から変換するトランスフォーム
    private AffineTransform transform = new AffineTransform();
    private int lastUpdateWidth;
    private int lastUpdateHeight;

    public void updateTransform(int width,int height) {
        lastUpdateWidth = width;
        lastUpdateHeight = height;
        double scaleX = (double) width/getViewPort().width;
        double scaleY = (double) height/getViewPort().height;
        scale = Math.min(scaleX,scaleY);
        int newWidth = Utils.round(getViewPort().width*scale);
        int newHeight = Utils.round(getViewPort().height*scale);
        drawX =(width-newWidth)/2;
        drawY = (height-newHeight)/2;
        transform = AffineTransform.getScaleInstance(1/scale,1/scale);
        transform.translate(-drawX,-drawY);
    }

    @Override
    public MaterialManager getMaterialManager() {
        return materialManager;
    }

    @Override
    public void draw(Graphics2D g,int width,int height) {
        updateTransform(width,height);
        int newWidth = Utils.round(getViewPort().width*scale);
        int newHeight = Utils.round(getViewPort().height*scale);
        g.setColor(Color.BLACK);
        g.fillRect(drawX,drawY,newWidth,newHeight);
        for (Layer layer:layers) {
            if (layer.isVisible()) {
                layer.draw(g);
            }
        }
        g.setColor(ViewUtils.BACKGROUND_COLOR);
        if (drawX == 0) {//上下に余白
            g.fillRect(0,0,width,drawY);
            g.fillRect(0,drawY+newHeight,width,drawY);
        } else {//左右に余白
            g.fillRect(0,0,drawX,height);
            g.fillRect(drawX+newWidth,0,drawX,height);
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
        updateTransform(lastUpdateWidth,lastUpdateHeight);
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
        try {
            //System.out.println(point+","+x+","+y+","+Point.convert(transform.inverseTransform(new Point2D.Double(x, y), null)));
            return Point.convert(transform.inverseTransform(new Point2D.Double(x, y), null));
        } catch (NoninvertibleTransformException e) {
            return null;
        }
    }

    @Override
    public Point convertFromScreenPosition(Point point,double z) {
        double distance = rectangle.width/2.0/Math.tan(pov/2);//カメラからスクリーンまでの距離
        double centerX = rectangle.x+rectangle.width/2.0;
        double centerY = rectangle.y+rectangle.height/2.0;
        double x = (distance+z)*(point.x()-rectangle.width/2.0)/distance+centerX;
        double y = (distance+z)*(point.y()-rectangle.height/2.0)/distance+centerY;
        return Point.convert(transform.transform(new Point2D.Double(x,y),null));
    }
}
