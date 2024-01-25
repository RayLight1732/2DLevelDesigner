package com.jp.daichi.designer;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.*;

import java.awt.*;

/**
 * 処理に役立つメソッドを集めたユーティリティクラス
 */
public class Util {



    /*
    public static Rectangle getRectangle(DesignerObject designerObject) {
        double x1 = designerObject.getPosition().x();
        double y1 = designerObject.getPosition().y();
        double x2 = x1+designerObject.getDimension().width();
        double y2 = y1+designerObject.getDimension().height();
        return new Rectangle( round(Math.min(x1,x2)),round(Math.min(y1,y2)),round(Math.abs(x2-x1)),round(Math.abs(y2-y1)));
    }*/

    public static Rectangle getRectangleOnScreen(Canvas canvas, DesignerObject designerObject) {
        double x1 = designerObject.getPosition().x();
        double y1 = designerObject.getPosition().y();
        Point p1 = canvas.convertToScreenPosition(new Point(x1,y1), designerObject.getZ());
        double x2 = x1+designerObject.getDimension().width();
        double y2 = y1+designerObject.getDimension().height();
        Point p2 = canvas.convertToScreenPosition(new Point(x2,y2), designerObject.getZ());
        return new Rectangle(round(Math.min(p1.x(),p2.x())),round(Math.min(p1.y(),p2.y())),round(Math.abs(p2.x()-p1.x())),round(Math.abs(p2.y()-p1.y())));
    }

    public static int round(double d) {
        return (int)Math.round(d);
    }

    public static Material getMaterial(ImageObject imageObject) {
        return imageObject.getCanvas().getMaterialManager().getInstance(imageObject.getMaterialUUID());
    }

}
