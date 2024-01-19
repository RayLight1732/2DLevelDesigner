package com.jp.daichi.designer.simple.manager;

import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.interfaces.UpdateObserver;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;
import com.jp.daichi.designer.simple.SimpleMaterial;
import com.jp.daichi.designer.simple.SimpleObservedObject;
import com.jp.daichi.designer.simple.editor.EditorMaterial;
import com.jp.daichi.designer.simple.editor.UpdateAction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class SimpleMaterialManager extends AManager<Material> implements MaterialManager {

    @Override
    protected String getName(Material target) {
        return target.getName();
    }

    @Override
    protected UUID getUUID(Material target) {
        return target.getUUID();
    }

    @Override
    protected void setObserverObject(Material target, UpdateObserver observer) {
        target.setUpdateObserver(observer);
    }
}
