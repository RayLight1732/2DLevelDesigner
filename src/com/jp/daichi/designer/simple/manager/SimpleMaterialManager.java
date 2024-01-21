package com.jp.daichi.designer.simple.manager;

import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;

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
    protected String getDefaultName() {
        return "NewMaterial";
    }
}
