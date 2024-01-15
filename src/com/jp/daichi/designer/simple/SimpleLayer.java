package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.Layer;
import com.jp.daichi.designer.interfaces.UpdateObserver;
import com.jp.daichi.designer.simple.editor.UpdateAction;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SimpleLayer implements Layer {

    private String name;
    private boolean isVisible = true;
    protected UpdateObserver observer;
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
        designerObject.setUpdateObserver(observer);
        Collections.sort(designerObjects);
        observer.update(this, UpdateAction.ADD);
    }

    @Override
    public boolean remove(DesignerObject designerObject) {
        boolean result = designerObjects.remove(designerObject);
        observer.update(this,UpdateAction.REMOVE);
        return result;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
        observer.update(this,UpdateAction.CHANGE_VISIBLY);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        observer.update(this,UpdateAction.CHANGE_NAME);
    }

    @Override
    public UpdateObserver getUpdateObserver() {
        return observer;
    }

    @Override
    public void setUpdateObserver(UpdateObserver observer) {
        this.observer = observer;
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
