package com.jp.daichi.designer;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public static Rectangle2D getRectangleOnScreen(Canvas canvas, DesignerObject designerObject) {
        double x1 = designerObject.getPosition().x();
        double y1 = designerObject.getPosition().y();
        Point p1 = canvas.convertToScreenPosition(new Point(x1, y1), designerObject.getZ());
        double x2 = x1 + designerObject.getDimension().width();
        double y2 = y1 + designerObject.getDimension().height();
        Point p2 = canvas.convertToScreenPosition(new Point(x2, y2), designerObject.getZ());
        return new Rectangle2D.Double (Math.min(p1.x(), p2.x()), Math.min(p1.y(), p2.y()), Math.abs(p2.x() - p1.x()), Math.abs(p2.y() - p1.y()));
    }

    public static int round(double d) {
        return (int) Math.round(d);
    }

    public static Material getMaterial(ImageObject imageObject) {
        return imageObject.getCanvas().getMaterialManager().getInstance(imageObject.getMaterialUUID());
    }

    /**
     * 画面上にデザイナーオブジェクトが表示されているか
     * @param designerObject デザイナーオブジェクト
     * @return 表示されているならtrue
     */
    public static boolean isShown(DesignerObject designerObject) {
        if (!designerObject.isVisible()) {
            return false;
        }
        Canvas canvas = designerObject.getCanvas();
        List<Layer> layers = canvas.getLayers().stream().map(uuid->canvas.getLayerManager().getInstance(uuid)).filter(Objects::nonNull).toList();
        for (Layer layer:layers) {
            if (layer.getObjects().contains(designerObject.getUUID())) {
                return layer.isVisible();
            }
        }
        return false;
    }
}
