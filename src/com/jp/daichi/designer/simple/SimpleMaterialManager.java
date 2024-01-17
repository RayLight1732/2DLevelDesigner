package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.interfaces.MaterialManager;
import com.jp.daichi.designer.interfaces.UpdateObserver;
import com.jp.daichi.designer.simple.editor.UpdateAction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SimpleMaterialManager extends SimpleObservedObject implements MaterialManager {

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
    public Material addMaterial(String name) {
        String resolvedName = resolveName(name);
        Material material = new SimpleMaterial(resolvedName,UUID.randomUUID());
        materials.add(material);
        sendUpdate(UpdateAction.ADD_MATERIAL);
        return material;
    }

    private String resolveName(String name) {
        int suffixNumber = 0;
        while (checkDuplication(name,suffixNumber)) {
            suffixNumber++;
        }
        return createSuffixedName(name,suffixNumber);
    }

    private boolean checkDuplication(String name,int suffixNumber) {
        String newName = createSuffixedName(name,suffixNumber);
        for (Material material:materials) {
            if (material.getName().equals(newName)) {
                return true;
            }
        }
        return false;
    }

    private String createSuffixedName(String name,int suffixNumber) {
        if (suffixNumber == 0) {
            return name;
        } else {
            return name+"("+suffixNumber+")";
        }
    }

    @Override
    public boolean removeMaterial(Material material) {
        boolean result = materials.remove(material);
        if (result) {
            sendUpdate(UpdateAction.REMOVE_MATERIAL);
        }
        return result;
    }

    @Override
    public List<Material> getMaterials() {
        return materials;
    }

}
