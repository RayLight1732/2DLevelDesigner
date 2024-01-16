package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.interfaces.UpdateObserver;
import com.jp.daichi.designer.simple.editor.UpdateAction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

public class SimpleMaterial implements Material {

    private BufferedImage image;
    private Point uvPoint = new Point(0,0);
    private SignedDimension uvDimension = new SignedDimension(0,0);
    private UpdateObserver observer;
    private String name;
    private final UUID uuid;

    public SimpleMaterial(String name,UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        getUpdateObserver().update(this,UpdateAction.CHANGE_NAME);
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void setImage(BufferedImage image) {
        this.image = image;
        observer.update(this,UpdateAction.CHANGE_IMAGE);
    }

    @Override
    public Point getUV() {
        return uvPoint;
    }

    @Override
    public void setUV(Point point) {
        this.uvPoint = point;
        getUpdateObserver().update(this, UpdateAction.CHANGE_UV);
    }

    @Override
    public SignedDimension getUVDimension() {
        return uvDimension;
    }

    @Override
    public void setUVDimension(SignedDimension dimension) {
        this.uvDimension = dimension;
        getUpdateObserver().update(this,UpdateAction.CHANGE_UV);
    }

    @Override
    public UpdateObserver getUpdateObserver() {
        return observer;
    }

    @Override
    public void setUpdateObserver(UpdateObserver observer) {
        this.observer = observer;
    }
}
