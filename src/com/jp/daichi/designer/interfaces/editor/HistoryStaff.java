package com.jp.daichi.designer.interfaces.editor;

import com.jp.daichi.designer.interfaces.Canvas;

import java.io.Serializable;

/**
 * 履歴の内容を表す
 */
public interface HistoryStaff extends Serializable {
    /**
     * この履歴の説明を取得する
     *
     * @return 履歴の説明
     */
    String description();

    /**
     * この履歴に登録されている内容を打ち消す操作を行う
     */
    void undo(Canvas canvas);

    /**
     * この履歴に登録されている内容を再び行う
     */
    void redo(Canvas canvas);

}
