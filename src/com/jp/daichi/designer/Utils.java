package com.jp.daichi.designer;

import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Frame;
import com.jp.daichi.designer.interfaces.Point;

import java.awt.*;

/**
 * 処理に役立つメソッドを集めたユーティリティクラス
 */
public class Utils {

    private static final Direction[] directions = {Direction.NORTH,Direction.NORTH_EAST,Direction.EAST,Direction.SOUTH_EAST,Direction.SOUTH,Direction.SOUTH_WEST,Direction.WEST,Direction.NORTH_WEST};

    private static Rectangle getAnchorRectangleOnScreen(DesignerObject designerObject, Direction direction) {
        Point converted = designerObject.getCanvas().convertToScreenPosition(designerObject.getPosition(),designerObject.getZ());
        double x = converted.x();
        double y = converted.y();
        //double w = designerObject.getDimension().width();
        //double h = designerObject.getDimension().height();
        Point converted2 = designerObject.getCanvas().convertToScreenPosition(designerObject.getPosition().add(new Point(designerObject.getDimension().width(),designerObject.getDimension().height())),designerObject.getZ());
        double w = converted2.x()-x;
        double h = converted2.y()-y;


        return switch (direction) {
            case NORTH -> getAnchorRectangleOnScreen(x+w/2,y);
            case NORTH_EAST -> getAnchorRectangleOnScreen(x,y);
            case EAST -> getAnchorRectangleOnScreen(x,y+h/2);
            case SOUTH_EAST -> getAnchorRectangleOnScreen(x,y+h);
            case SOUTH -> getAnchorRectangleOnScreen(x+w/2,y+h);
            case SOUTH_WEST -> getAnchorRectangleOnScreen(x+w,y+h);
            case WEST -> getAnchorRectangleOnScreen(x+w,y+h/2);
            case NORTH_WEST -> getAnchorRectangleOnScreen(x+w,y);
            default -> null;
        };
    }

    private static Rectangle getAnchorRectangleOnScreen(double x, double y) {
        return new Rectangle(round(x-ro),round(y-ro),rs,rs);
    }

    private static final Stroke dashStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, new float[] {6}, 0);
    private static final Stroke normalStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
    private static final int rs = 7;//RectSize
    private static final int ro = 3;//RectOffset
    /**
     * 選択されているオブジェクト群の枠線を描画する
     * @param graphics2D グラフィックオブジェクト
     * @param frame フレーム
     */
    public static void drawSelectedFrame(Graphics2D graphics2D, Frame frame) {
        if (frame.getSelectedObjectCount() == 0) {
            return;
        }
        Rectangle frameRectangle = getRectangleOnScreen(frame.getCanvas(),frame);

        graphics2D.setColor(Color.BLACK);
        graphics2D.setStroke(dashStroke);
        graphics2D.draw(frameRectangle);

        graphics2D.setStroke(normalStroke);
        for (int i = directions.length-1;i >= 0;i--) {
            Direction direction = directions[i];
            Rectangle rectangle = getAnchorRectangleOnScreen(frame,direction);
            graphics2D.setColor(Color.WHITE);
            graphics2D.fill(rectangle);
            graphics2D.setColor(Color.BLACK);
            graphics2D.draw(rectangle);
        }
    }


    /**
     * キャンバス上の他のオブジェクトの位置に合わせてスナップされた点を取得する
     * @param canvas キャンバス
     * @param point 元の点
     * @return スナップされた後の点
     */
    public static Point getSnappedPosition(Canvas canvas, Point point) {
        return point;
        //TODO
    }

    /**
     * 指定された点が形を変えるためのアンカーをさしているとき、その方角を返す
     * @param frame フレーム
     * @param point 判定対象の点
     * @return 方角 さしていない場合はNONEを返す
     * @see Direction#NONE
     * @see Direction#CENTER
     * @see Direction#NORTH
     * @see Direction#NORTH_EAST
     * @see Direction#EAST
     * @see Direction#SOUTH_EAST
     * @see Direction#SOUTH
     * @see Direction#SOUTH_WEST
     * @see Direction#WEST
     * @see Direction#NORTH_WEST
     */
    public static Direction getSizeChangeAnchor(Frame frame, Point point) {
        if (frame.getSelectedObjectCount() == 0) {
            return Direction.NONE;
        }
        for (Direction direction:directions) {
            if (getAnchorRectangleOnScreen(frame,direction).contains(point.x(),point.y())) {
                return direction;
            }
        }
        if (getRectangleOnScreen(frame.getCanvas(),frame).contains(point.x(),point.y())) {
            return Direction.CENTER;
        }
        return Direction.NONE;
    }

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
