package com.jp.daichi.designer.interfaces.editor;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.ObservedObject;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.interfaces.manager.LayerManager;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;

import java.util.List;

/**
 * 履歴を管理する
 */
public interface History extends ObservedObject {
    /**
     * 現状の状態を表す履歴のインデックスを返す
     * @return 現在の状態を表す履歴のインデックス -1は履歴が存在しないことを表す
     */
    int getHead();

    /**
     * 履歴を現在のHeadの後ろに追加する
     * @param historyStaff 追加する履歴
     */
    void add(HistoryStaff historyStaff);

    /**
     * 現在登録されている履歴を全て取得する
     * @return 現在登録されている全ての履歴
     */
    List<HistoryStaff> getHistories();

    /**
     * Undoが呼び出される対象を取得する
     * @return Undoが呼び出される対象
     */
    HistoryStaff getUndoTarget();

    /**
     * Redoが呼び出される対象を取得する
     * @return Redoが呼び出される対象
     */
    HistoryStaff getRedoTarget();

    /**
     * 取り消しを行う
     * @param canvas キャンバス
     */
    void undo(Canvas canvas);

    /**
     * 取り消しを行えるかどうかを取得する
     * @return 取り消しを行えるかどうか
     */
    boolean canUndo();

    /**
     * やり直しを行う
     * @param canvas キャンバス
     */
    void redo(Canvas canvas);

    /**
     * やり直しを行えるかどうかを取得する
     * @return やり直しを行えるかどうか
     */
    boolean canRedo();

}
