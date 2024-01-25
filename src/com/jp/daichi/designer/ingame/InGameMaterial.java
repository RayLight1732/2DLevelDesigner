package com.jp.daichi.designer.ingame;

import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;
import com.jp.daichi.designer.simple.BufferedImageSerializer;
import com.jp.daichi.designer.simple.SimpleMaterial;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * ゲーム用のマテリアルの実装
 */
public class InGameMaterial extends SimpleMaterial {
    /**
     * デシリアライズを行う
     *
     * @param materialManager マネージャー
     * @param serialized      シリアライズされたデータ
     * @return デシリアライズされた結果
     */
    public static InGameMaterial deserialize(MaterialManager materialManager, Map<String, Object> serialized) {
        try {
            String name = (String) serialized.get("Name");
            UUID uuid = (UUID) serialized.get("UUID");
            Point uv = (Point)serialized.get("UV");
            SignedDimension uvDimension = (SignedDimension) serialized.get("UVDimension");
            Objects.requireNonNull(name);
            Objects.requireNonNull(uuid);
            Objects.requireNonNull(uv);
            Objects.requireNonNull(uvDimension);
            BufferedImageSerializer biSerializer = (BufferedImageSerializer) serialized.get("Image");
            return new InGameMaterial(name,uuid,biSerializer == null ? null:biSerializer.getImage(),uv,uvDimension,materialManager);
        } catch (NullPointerException | ClassCastException e) {
            return null;
        }
    }

    /**
     * 新しいマテリアルを作成する
     *
     * @param name            名前
     * @param uuid            UUID
     * @param materialManager マネージャー
     */
    public InGameMaterial(String name, UUID uuid, MaterialManager materialManager) {
        super(name, uuid, materialManager);
    }

    /**
     * 新しいマテリアルを作成する
     *
     * @param name            名前
     * @param uuid            UUID
     * @param image           画像
     * @param uv UV座標
     * @param uvDimension UVの描画領域
     * @param materialManager マネージャー
     */
    public InGameMaterial(String name, UUID uuid, BufferedImage image,Point uv,SignedDimension uvDimension, MaterialManager materialManager) {
        super(name, uuid, image,uv,uvDimension, materialManager);
    }
}
