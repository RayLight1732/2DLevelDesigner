package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.interfaces.ObservedObject;
import com.jp.daichi.designer.interfaces.UpdateAction;
import com.jp.daichi.designer.interfaces.UpdateObserver;

public abstract class SimpleObservedObject implements ObservedObject {
    private UpdateObserver observer;

    @Override
    public UpdateObserver getUpdateObserver() {
        return observer;
    }

    @Override
    public void setUpdateObserver(UpdateObserver observer) {
        this.observer = observer;
    }

    /**
     * UpdateObserverがnullでないならば、updateを呼び出す
     *
     * @param action アクション
     */
    protected void sendUpdate(UpdateAction action) {
        if (observer != null) {
            observer.update(this, action);
        }
    }
}
