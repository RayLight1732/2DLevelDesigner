package com.jp.daichi.designer.interfaces;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 更新を監視する
 */
public class UpdateObserver {
    private boolean isUpdated = false;

    private final List<ObserverRunnable> list = new ArrayList<>();

    public void addRunnable(ObserverRunnable runnable) {
        list.add(runnable);
    }

    public void removeRunnable(ObserverRunnable runnable) {
        list.remove(runnable);
    }

    /**
     * 更新されたときに呼び出される
     */
    public void update(ObservedObject target, UpdateAction action) {
        isUpdated = true;
        for (ObserverRunnable runnable : list) {
            SwingUtilities.invokeLater(() -> runnable.run(target, action));
        }
    }

    /**
     * 更新されたかどうか
     *
     * @return 更新されたならtrue
     */
    public boolean isUpdated() {
        return isUpdated;
    }

    /**
     * 破棄する
     */
    public void reset() {
        isUpdated = false;
    }

    public interface ObserverRunnable {
        void run(ObservedObject target, UpdateAction action);
    }
}
