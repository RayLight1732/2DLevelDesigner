package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.interfaces.UpdateAction;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;

import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.UUID;

/**
 * 基本的なマテリアルの実装
 */
public class SimpleMaterial extends SimpleObservedObject implements Material {

    private BufferedImage image;
    private Point uvPoint;
    private SignedDimension uvDimension;
    private String name;
    private final UUID uuid;
    private final MaterialManager materialManager;

    /**
     * 新しいマテリアルを作成する
     *
     * @param name            名前
     * @param uuid            UUID
     * @param materialManager マネージャー
     */
    public SimpleMaterial(String name, UUID uuid, MaterialManager materialManager) {
        this(name, uuid, null, new Point(0, 0), new SignedDimension(0, 0), materialManager);
    }

    /**
     * 新しいマテリアルを作成する
     *
     * @param name            名前
     * @param uuid            UUID
     * @param image           画像
     * @param uv              UV座標
     * @param uvDimension     UVの描画領域
     * @param materialManager マネージャー
     */
    public SimpleMaterial(String name, UUID uuid, BufferedImage image, Point uv, SignedDimension uvDimension, MaterialManager materialManager) {
        Objects.requireNonNull(uv);
        Objects.requireNonNull(uvDimension);
        this.name = name;
        this.uuid = uuid;
        this.materialManager = materialManager;
        this.image = image;
        this.uvPoint = uv;
        this.uvDimension = uvDimension;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        if (!this.name.equals(name)) {
            this.name = materialManager.resolveName(getUUID(), name);
            sendUpdate(UpdateAction.CHANGE_NAME);
        }
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
        sendUpdate(UpdateAction.CHANGE_IMAGE);
    }

    @Override
    public Point getUV() {
        return uvPoint;
    }

    @Override
    public void setUV(Point point) {
        this.uvPoint = point;
        sendUpdate(UpdateAction.CHANGE_UV);
    }

    @Override
    public SignedDimension getUVDimension() {
        return uvDimension;
    }

    @Override
    public void setUVDimension(SignedDimension dimension) {
        this.uvDimension = dimension;
        sendUpdate(UpdateAction.CHANGE_UV);
    }

    @Override
    public int compareTo(Material o) {
        return getName().compareTo(o.getName());
    }
}
