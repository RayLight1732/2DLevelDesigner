package com.jp.daichi.designer.simple.manager;

import com.jp.daichi.designer.interfaces.ObservedObject;
import com.jp.daichi.designer.interfaces.UpdateAction;
import com.jp.daichi.designer.interfaces.UpdateObserver;
import com.jp.daichi.designer.interfaces.manager.IManager;
import com.jp.daichi.designer.simple.SimpleObservedObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * マネージャーの基底クラス
 *
 * @param <T> 管理対象の型
 */
public abstract class AManager<T extends ObservedObject> extends SimpleObservedObject implements IManager<T> {
    /**
     * このレイヤーが管理するオブジェクトのリスト
     */
    protected final List<T> instances = new ArrayList<>();


    /**
     * インスタンスの追加を行うと同時に、このマネージャーに設定されているオブザーバーを対象のインスタンスに設定し、さらにオブザーバーに通知を行う
     *
     * @param instance 追加するインスタンス
     */
    protected void addInstance(T instance) {
        instances.add(instance);
        instance.setUpdateObserver(getUpdateObserver());
        sendUpdate(UpdateAction.ADD);
    }

    @Override
    public T getInstance(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        for (T instance : instances) {
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
        for (T instance : instances) {
            instance.setUpdateObserver(observer);
        }
    }

    @Override
    public String resolveName(UUID uuid, String name) {
        if (name.isEmpty()) {
            name = getDefaultName();
        }
        int suffixNumber = 0;
        while (isDuplicate(uuid, name, suffixNumber)) {
            suffixNumber++;
        }
        return createSuffixedName(name, suffixNumber);
    }

    /**
     * 名前が重複しているかどうか
     *
     * @param uuid         UUID
     * @param name         名前
     * @param suffixNumber 接尾子
     * @return 重複しているならtrue
     */
    protected boolean isDuplicate(UUID uuid, String name, int suffixNumber) {
        String newName = createSuffixedName(name, suffixNumber);
        for (T instance : instances) {
            if (getName(instance).equals(newName)) {
                if (uuid == null || getUUID(instance).compareTo(uuid) != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private String createSuffixedName(String name, int suffixNumber) {
        if (suffixNumber == 0) {
            return name;
        } else {
            return name + "(" + suffixNumber + ")";
        }
    }

    protected abstract String getName(T target);

    protected abstract UUID getUUID(T target);

    protected abstract String getDefaultName();

    @Override
    public List<T> getAllInstances() {
        return new ArrayList<>(instances);
    }
}
