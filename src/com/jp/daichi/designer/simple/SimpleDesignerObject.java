package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.interfaces.*;

import java.awt.geom.Rectangle2D;
import java.util.UUID;


/**
 * デザイナーオブジェクトの基本的な実装
 */
public abstract class SimpleDesignerObject extends SimpleObservedObject implements DesignerObject {

    private final Canvas canvas;
    private Point position;
    private SignedDimension dimension;
    private int priority = 0;
    private boolean isVisible = true;

    private double z = 0;
    private String name;
    private final UUID uuid;
    private final DesignerObjectType type;

    /**
     * デザイナーオブジェクトのインスタンスを作成する
     * @param name このオブジェクトの名前
     * @param uuid UUID
     * @param type タイプ
     * @param canvas キャンバス
     * @param position 座標
     * @param dimension 表示領域
     */
    public SimpleDesignerObject(String name,UUID uuid,DesignerObjectType type, Canvas canvas, Point position, SignedDimension dimension) {
        this(name,uuid,type,canvas,position,dimension,0);
    }

    /**
     * デザイナーオブジェクトのインスタンスを作成する
     * @param name このオブジェクトの名前
     * @param uuid UUID
     * @param type タイプ
     * @param canvas キャンバス
     * @param position 座標
     * @param dimension 表示領域
     * @param priority 優先度
     */
    public SimpleDesignerObject(String name,UUID uuid,DesignerObjectType type, Canvas canvas, Point position, SignedDimension dimension,int priority) {
        this.name = name;
        this.uuid = uuid;
        this.type = type;
        this.canvas = canvas;
        this.position = position;
        this.dimension = dimension;
        this.priority = priority;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point point) {
        if (this.position == null || point == null || point.x() != this.position.x() || point.y() != this.position.y()) {
            this.position = point;
            sendUpdate(UpdateAction.CHANGE_RECTANGLE);
        }
    }

    @Override
    public SignedDimension getDimension() {
        return dimension;
    }

    @Override
    public void setDimension(SignedDimension dimension) {
        if (this.dimension == null || dimension == null || dimension.width() != this.dimension.width() || dimension.height() != this.dimension.height()) {
            this.dimension = dimension;
            sendUpdate(UpdateAction.CHANGE_RECTANGLE);
        }
    }


    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
        sendUpdate(UpdateAction.CHANGE_PRIORITY);
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
        sendUpdate(UpdateAction.CHANGE_VISIBLY);
    }

    @Override
    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public boolean isSelectable() {
        return isVisible();
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public void setZ(double z) {
        this.z = z;
        sendUpdate(UpdateAction.CHANGE_Z);
    }

    @Override
    public int compareTo(DesignerObject o) {
        double d = getZ()- o.getZ();
        if (d == 0) {
            d = o.getPriority()-getPriority();
            if (d == 0) {
                return getName().compareTo(o.getName());
            }
        }

        return (int) Math.signum(d);

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        if (!name.equals(this.name)) {
            this.name = canvas.getDesignerObjectManager().resolveName(getUUID(),name);
            sendUpdate(UpdateAction.CHANGE_NAME);
        }
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public Rectangle2D getRectangle() {
        double x1 = position.x();
        double y1 = position.y();
        double x2 = x1+dimension.width();
        double y2 = y1+dimension.height();
        return new Rectangle2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2));
    }

    @Override
    public DesignerObjectType getType() {
        return type;
    }
}
