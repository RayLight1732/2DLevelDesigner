package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.DesignerObjectType;
import com.jp.daichi.designer.interfaces.Layer;
import com.jp.daichi.designer.interfaces.UpdateAction;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * 基本的なレイヤーの実装
 */
public class SimpleLayer extends SimpleObservedObject implements Layer {

    private String name;
    private boolean isVisible = true;
    private final UUID uuid;
    private final DesignerObjectType type;

    /**
     * 登録されているデザイナーオブジェクトのリスト
     */
    protected final List<UUID> designerObjects = new ArrayList<>();

    /**
     * 新しいレイヤーのインスタンスを作成する
     *
     * @param name 名前
     * @param uuid UUID
     * @param type 管理するデザイナーオブジェクトのタイプ
     */
    public SimpleLayer(String name, UUID uuid, DesignerObjectType type) {
        this.name = name;
        this.uuid = uuid;
        this.type = type;
    }

    @Override
    public List<UUID> getObjects() {
        return new ArrayList<>(designerObjects);
    }

    @Override
    public void add(UUID designerObjectUUID) {
        designerObjects.add(designerObjectUUID);
        Collections.sort(designerObjects);
        sendUpdate(UpdateAction.ADD);
    }

    @Override
    public boolean remove(UUID designerObject) {
        boolean result = designerObjects.remove(designerObject);
        sendUpdate(UpdateAction.REMOVE);
        return result;
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        sendUpdate(UpdateAction.CHANGE_NAME);
    }

    @Override
    public void draw(Graphics2D g, DesignerObjectManager designerObjectManager) {
        if (isVisible()) {
            designerObjects.stream().map(designerObjectManager::getInstance).filter(Objects::nonNull).filter(DesignerObject::isVisible).sorted(Comparator.reverseOrder()).forEach(it -> it.draw(g));
        }
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public DesignerObjectType getObjectType() {
        return type;
    }

    @Override
    public int compareTo(Layer o) {
        return DesignerObjectType.valueOf(getObjectType().name()).ordinal() - DesignerObjectType.valueOf(o.getObjectType().name()).ordinal();
    }
}
