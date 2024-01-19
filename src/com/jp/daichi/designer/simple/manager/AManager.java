package com.jp.daichi.designer.simple.manager;

import com.jp.daichi.designer.interfaces.ObservedObject;
import com.jp.daichi.designer.interfaces.UpdateObserver;
import com.jp.daichi.designer.interfaces.manager.IManager;
import com.jp.daichi.designer.simple.SimpleObservedObject;
import com.jp.daichi.designer.simple.editor.UpdateAction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * マネージャーの基底クラス
 * @param <T> 管理対象の型
 */
public abstract class AManager<T extends ObservedObject> extends SimpleObservedObject implements IManager<T> {
    protected final List<T> instances = new ArrayList<>();

    @Override
    public T createInstance(String name) {
        T instance = createManagedObjectInstance(resolveName(name));
        if (instance != null) {
            setObserverObject(instance, getUpdateObserver());
            instances.add(instance);
            instance.setUpdateObserver(getUpdateObserver());
            sendUpdate(UpdateAction.ADD);
            return instance;
        } else {
            return null;
        }
    }

    @Override
    public T getInstance(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        for (T instance:instances) {
            if (getUUID(instance).compareTo(uuid) == 0) {
                return instance;
            }
        }
        return null;
    }

    @Override
    public boolean removeInstance(T object) {
        boolean result = instances.remove(object);
        if (result) {
            sendUpdate(UpdateAction.REMOVE);
        }
        return result;
    }

    @Override
    public void setUpdateObserver(UpdateObserver observer) {
        super.setUpdateObserver(observer);
        for (T instance:instances) {
            instance.setUpdateObserver(observer);
        }
    }

    protected String resolveName(String name) {
        int suffixNumber = 0;
        while (checkDuplication(name,suffixNumber)) {
            suffixNumber++;
        }
        return createSuffixedName(name,suffixNumber);
    }

    protected boolean checkDuplication(String name,int suffixNumber) {
        String newName = createSuffixedName(name,suffixNumber);
        for (T instance:instances) {
            if (getName(instance).equals(newName)) {
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

    protected abstract T createManagedObjectInstance(String resolvedName);

    protected abstract String getName(T target);

    protected abstract UUID getUUID(T target);

    protected abstract void setObserverObject(T target, UpdateObserver observer);


    @Override
    public List<T> getAllInstances() {
        return new ArrayList<>(instances);
    }
}
