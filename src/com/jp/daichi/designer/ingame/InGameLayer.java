package com.jp.daichi.designer.ingame;

import com.jp.daichi.designer.interfaces.DesignerObjectType;
import com.jp.daichi.designer.interfaces.Layer;
import com.jp.daichi.designer.simple.SimpleLayer;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * ゲーム用のレイヤーの実装
 */
public class InGameLayer extends SimpleLayer {

    /**
     * デシリアライズを行う
     *
     * @param map シリアライズされたデータ
     * @return デシリアライズされた結果
     */
    public static Layer deserialize(Map<String, Object> map) {
        try {
            String name = (String) map.get("Name");
            UUID uuid = (UUID) map.get("UUID");
            List<UUID> objects = (List<UUID>) map.get("Objects");
            DesignerObjectType type = (DesignerObjectType) map.get("Type");
            Objects.requireNonNull(name);
            Objects.requireNonNull(uuid);
            Objects.requireNonNull(objects);
            Objects.requireNonNull(type);
            Layer result = new InGameLayer(name, uuid, type);
            for (UUID designerObjectsUUID : objects) {
                result.add(designerObjectsUUID);
            }
            return result;
        } catch (NullPointerException | ClassCastException e) {
            return null;
        }
    }

    /**
     * 新しいレイヤーのインスタンスを作成する
     *
     * @param name 名前
     * @param uuid UUID
     * @param type このレイヤーが管理するデザイナーオブジェクトのタイプ
     */
    public InGameLayer(String name, UUID uuid, DesignerObjectType type) {
        super(name, uuid, type);
    }
}
