package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.interfaces.MaterialManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SimpleMaterialManager implements MaterialManager {

    private final List<Material> materials = new ArrayList<>();

    @Override
    public Material getMaterial(UUID uuid) {
        for (Material material:materials) {
            if (material.getUUID().compareTo(uuid) == 0) {
                return material;
            }
        }
        return null;
    }

    @Override
    public boolean registerMaterial(Material material) {
        if (materials.contains(material)) {
            return false;
        } else {
            materials.add(material);
            return true;
        }
    }

    @Override
    public boolean removeMaterial(Material material) {
        return materials.remove(material);
    }

    @Override
    public List<Material> getMaterials() {
        return materials;
    }
}
