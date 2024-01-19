package com.jp.daichi.designer.simple.editor.history;

import com.jp.daichi.designer.interfaces.editor.HistoryStaff;
import com.jp.daichi.designer.interfaces.editor.PermanentObject;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.interfaces.manager.LayerManager;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;

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
    public void undo(DesignerObjectManager designerObjectManager, LayerManager layerManager, MaterialManager materialManager) {
        T target = getTarget(designerObjectManager, layerManager, materialManager);
        if (target != null) {
            target.setSaveHistory(false);
            setValue(target,oldValue);
            target.setSaveHistory(true);
        }
    }

    @Override
    public void redo(DesignerObjectManager designerObjectManager, LayerManager layerManager, MaterialManager materialManager) {
        T target = getTarget(designerObjectManager, layerManager, materialManager);
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
     * null許容
     * @param designerObjectManager デザイナーオブジェクトマネージャー
     * @param layerManager          レイヤーマネージャー
     * @param materialManager       マテリアルマネージャー
     * @return ターゲットとなるオブジェクトのインスタンス
     */
    public abstract T getTarget(DesignerObjectManager designerObjectManager, LayerManager layerManager, MaterialManager materialManager);

}
