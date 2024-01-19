package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.Layer;
import com.jp.daichi.designer.interfaces.UpdateObserver;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.simple.editor.UpdateAction;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SimpleLayer extends SimpleObservedObject implements Layer {

    private String name;
    private boolean isVisible = true;
    private final UUID uuid;
    protected final List<UUID> designerObjects = new ArrayList<>();

    public SimpleLayer(String name,UUID uuid) {
        this.name = name;
        this.uuid = uuid;
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
    public void draw(Graphics2D g,DesignerObjectManager designerObjectManager) {
        if (isVisible()) {
            designerObjects.stream().map(designerObjectManager::getDesignerObject).filter(DesignerObject::isVisible).sorted(Comparator.reverseOrder()).forEach(it ->it.draw(g));
        }
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
}
