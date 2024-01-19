package com.jp.daichi.designer.interfaces.editor;

import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.interfaces.manager.LayerManager;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;

import java.io.Serializable;
import java.util.Map;

/**
 * 履歴の内容を表す
 */
public interface HistoryStaff extends Serializable {
    /**
     * この履歴の説明を取得する
     * @return 履歴の説明
     */
    String getDescription();

    /**
     * この履歴に登録されている内容を打ち消す操作を行う
     */
    void undo(DesignerObjectManager designerObjectManager, LayerManager layerManager, MaterialManager materialManager);

    /**
     * この履歴に登録されている内容を再び行う
     */
    void redo(DesignerObjectManager designerObjectManager, LayerManager layerManager,MaterialManager materialManager);

}
