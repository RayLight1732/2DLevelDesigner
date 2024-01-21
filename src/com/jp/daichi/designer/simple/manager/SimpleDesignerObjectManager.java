package com.jp.daichi.designer.simple.manager;

import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.Layer;
import com.jp.daichi.designer.interfaces.UpdateObserver;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;

import java.util.UUID;

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
}
