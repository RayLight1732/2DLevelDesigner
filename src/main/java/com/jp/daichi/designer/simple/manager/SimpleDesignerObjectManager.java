package com.jp.daichi.designer.simple.manager;

import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.DesignerObjectType;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.simple.DesignerObjectSerializer;

import java.util.Map;
import java.util.UUID;

/**
 * 基本的なデザイナーオブジェクトマネージャーの実装
 */
public abstract class SimpleDesignerObjectManager extends AManager<DesignerObject> implements DesignerObjectManager {

    @Override
    protected String getName(DesignerObject target) {
        return target.getName();
    }

    @Override
    protected UUID getUUID(DesignerObject target) {
        return target.getUUID();
    }

    @Override
    protected String getDefaultName() {
        return "NewObject";
    }

    @Override
    public <T extends DesignerObject> T createInstance(String name, DesignerObjectType type) {
        T designerObject = createInstance(resolveName(null, name), UUID.randomUUID(), type);
        if (designerObject != null) {
            addInstance(designerObject);
            return designerObject;
        } else {
            return null;
        }
    }

    /**
     * 新しいデザイナーオブジェクトのインスタンスを作成する
     *
     * @param resolvedName 重複が解消された名前
     * @param uuid         UUID
     * @param type         タイプ
     * @param <T>          typeが表すクラス
     * @return 新しいデザイナーオブジェクトのインスタンス
     */
    protected abstract <T extends DesignerObject> T createInstance(String resolvedName, UUID uuid, DesignerObjectType type);

    @Override
    public DesignerObject deserializeManagedObject(Map<String, Object> map) {
        return deserializeManagedObject(map,false);
    }

    @Override
    public DesignerObject deserializeManagedObject(Map<String, Object> map, boolean resolveDuplication) {
        DesignerObjectSerializer.DeserializedData deserializedData = DesignerObjectSerializer.deserialize(map);
        if (deserializedData != null) {
            if (resolveDuplication) {
                deserializedData = new DesignerObjectSerializer.DeserializedData(deserializedData.type(),resolveName(null,deserializedData.name()),deserializedData.uuid(),deserializedData.position(),deserializedData.z(),deserializedData.dimension(),deserializedData.priority());
            }
            if (checkDuplicateUUID(deserializedData.uuid())) {
                if (resolveDuplication) {
                    deserializedData = new DesignerObjectSerializer.DeserializedData(deserializedData.type(),deserializedData.name(),UUID.randomUUID(),deserializedData.position(),deserializedData.z(),deserializedData.dimension(),deserializedData.priority());
                } else {
                    throw new IllegalArgumentException("duplicate UUID");
                }
            }
            DesignerObject designerObject = deserializeManagedObject(deserializedData, map);
            if (designerObject != null) {
                addInstance(designerObject);
            }
            return designerObject;
        } else {
            return null;
        }
    }

    /**
     * デシリアライズを行うと同時に登録を行う。
     *
     * @param deserializedData デシリアライズされたデータ
     * @param map              シリアライズされたデータ
     * @return デシリアライズの結果
     */
    protected abstract DesignerObject deserializeManagedObject(DesignerObjectSerializer.DeserializedData deserializedData, Map<String, Object> map);

    protected boolean checkDuplicateUUID(UUID uuid) {
        for (DesignerObject designerObject: instances) {
            if (uuid.compareTo(designerObject.getUUID()) == 0) {
                return true;
            }
        }
        return false;
    }
}
