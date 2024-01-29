package com.jp.daichi.designer.editor;

import com.jp.daichi.designer.Util;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.Direction;
import com.jp.daichi.designer.interfaces.Point;

import java.awt.*;
import java.awt.geom.Rectangle2D;


/**
 * エディター用のユーティリティクラス
 */
public class EditorUtil {

    /**
     * キャンバス上の他のオブジェクトの位置に合わせてスナップされた点を取得する
     *
     * @param canvas キャンバス
     * @param point  元の点
     * @return スナップされた後の点
     */
    public static Point getSnappedPosition(Canvas canvas, Point point) {
        return point;
        //TODO
    }

    /**
     * 指定された点が形を変えるためのアンカーをさしているとき、その方角を返す
     *
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
        for (Direction direction : directions) {
            if (getAnchorRectangleOnScreen(frame, direction).contains(point.x(), point.y())) {
                return direction;
            }
        }
        if (Util.getRectangleOnScreen(frame.getCanvas(), frame).contains(point.x(), point.y())) {
            return Direction.CENTER;
        }
        return Direction.NONE;
    }

    private static final Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};

    private static Rectangle getAnchorRectangleOnScreen(DesignerObject designerObject, Direction direction) {
        Point converted = designerObject.getCanvas().convertToScreenPosition(designerObject.getPosition(), designerObject.getZ());
        double x = converted.x();
        double y = converted.y();
        //double w = designerObject.getDimension().width();
        //double h = designerObject.getDimension().height();
        Point converted2 = designerObject.getCanvas().convertToScreenPosition(designerObject.getPosition().add(new Point(designerObject.getDimension().width(), designerObject.getDimension().height())), designerObject.getZ());
        double w = converted2.x() - x;
        double h = converted2.y() - y;


        return switch (direction) {
            case NORTH -> getAnchorRectangleOnScreen(x + w / 2, y);
            case NORTH_EAST -> getAnchorRectangleOnScreen(x, y);
            case EAST -> getAnchorRectangleOnScreen(x, y + h / 2);
            case SOUTH_EAST -> getAnchorRectangleOnScreen(x, y + h);
            case SOUTH -> getAnchorRectangleOnScreen(x + w / 2, y + h);
            case SOUTH_WEST -> getAnchorRectangleOnScreen(x + w, y + h);
            case WEST -> getAnchorRectangleOnScreen(x + w, y + h / 2);
            case NORTH_WEST -> getAnchorRectangleOnScreen(x + w, y);
            default -> null;
        };
    }

    private static Rectangle getAnchorRectangleOnScreen(double x, double y) {
        return new Rectangle(Util.round(x - ro), Util.round(y - ro), rs, rs);
    }

    private static final Stroke dashStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, new float[]{6}, 0);
    private static final Stroke normalStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
    private static final int rs = 7;//RectSize
    private static final int ro = 3;//RectOffset

    /**
     * 選択されているオブジェクト群の枠線を描画する
     *
     * @param graphics2D グラフィックオブジェクト
     * @param frame      フレーム
     */
    public static void drawSelectedFrame(Graphics2D graphics2D, Frame frame) {
        if (frame.getSelectedObjectCount() == 0) {
            return;
        }
        Rectangle2D frameRectangle = Util.getRectangleOnScreen(frame.getCanvas(), frame);

        graphics2D.setColor(Color.BLACK);
        graphics2D.setStroke(dashStroke);
        graphics2D.draw(frameRectangle);

        graphics2D.setStroke(normalStroke);
        for (int i = directions.length - 1; i >= 0; i--) {
            Direction direction = directions[i];
            Rectangle rectangle = getAnchorRectangleOnScreen(frame, direction);
            graphics2D.setColor(Color.WHITE);
            graphics2D.fill(rectangle);
            graphics2D.setColor(Color.BLACK);
            graphics2D.draw(rectangle);
        }
    }


}
