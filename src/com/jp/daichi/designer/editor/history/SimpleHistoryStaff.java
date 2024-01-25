package com.jp.daichi.designer.editor.history;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.editor.HistoryStaff;
import com.jp.daichi.designer.interfaces.editor.PermanentObject;

import java.util.UUID;

public abstract class SimpleHistoryStaff<T extends PermanentObject,U> implements HistoryStaff {

    protected UUID uuid;
    protected final U oldValue;
    protected final U newValue;

    public SimpleHistoryStaff(UUID uuid,U oldValue,U newValue) {
        this.uuid = uuid;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public void undo(Canvas canvas) {
        T target = getTarget(canvas);
        if (target != null) {
            target.setSaveHistory(false);
            setValue(target,oldValue);
            target.setSaveHistory(true);
        }
    }

    @Override
    public void redo(Canvas canvas) {
        T target = getTarget(canvas);
        if (target != null) {
            target.setSaveHistory(false);
            setValue(target,newValue);
            target.setSaveHistory(true);
        }
    }

    /**
     * 値を設定する
     * @param target 値を設定するターゲットのインスタンス nullは取らない
     * @param value  値
     */
    public abstract void setValue(T target,U value);

    /**
     * ターゲットとなるオブジェクトのインスタンスを取得する
     * @param canvas キャンバス
     * @return ターゲットとなるオブジェクトのインスタンス null許容
     */
    public abstract T getTarget(Canvas canvas);

}
