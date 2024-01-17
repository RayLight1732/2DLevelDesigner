package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.Layer;
import com.jp.daichi.designer.interfaces.UpdateObserver;
import com.jp.daichi.designer.simple.editor.UpdateAction;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SimpleLayer extends SimpleObservedObject implements Layer {

    private String name;
    private boolean isVisible = true;
    protected final List<DesignerObject> designerObjects = new ArrayList<>();

    public SimpleLayer(String name) {
        this.name = name;
    }

    @Override
    public List<DesignerObject> getObjects() {
        return new ArrayList<>(designerObjects);
    }

    @Override
    public void add(DesignerObject designerObject) {
        designerObjects.add(designerObject);
        designerObject.setUpdateObserver(getUpdateObserver());
        Collections.sort(designerObjects);
        sendUpdate(UpdateAction.ADD);
    }

    @Override
    public boolean remove(DesignerObject designerObject) {
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
    public void setUpdateObserver(UpdateObserver observer) {
        super.setUpdateObserver(observer);
        for (DesignerObject designerObject:designerObjects) {
            designerObject.setUpdateObserver(observer);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (isVisible()) {
            designerObjects.stream().filter(DesignerObject::isVisible).sorted(Comparator.reverseOrder()).forEach(it ->it.draw(g));
        }
    }
}
