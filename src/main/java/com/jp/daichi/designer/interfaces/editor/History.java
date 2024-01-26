package com.jp.daichi.designer.interfaces.editor;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.ObservedObject;

import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;

/**
 * 履歴を管理する
 */
public interface History extends ObservedObject, Serializable {
    /**
     * 現状の状態を表す履歴のインデックスを返す
     *
     * @return 現在の状態を表す履歴のインデックス -1は履歴が存在しないことを表す
     */
    int getHead();

    /**
     * 履歴を現在のHeadの後ろに追加する
     *
     * @param historyStaff 追加する履歴
     */
    void add(HistoryStaff historyStaff);

    /**
     * 現在登録されている履歴を全て取得する
     *
     * @return 現在登録されている全ての履歴
     */
    List<HistoryStaff> getHistories();

    /**
     * Undoが呼び出される対象を取得する
     *
     * @return Undoが呼び出される対象
     */
    HistoryStaff getUndoTarget();

    /**
     * Redoが呼び出される対象を取得する
     *
     * @return Redoが呼び出される対象
     */
    HistoryStaff getRedoTarget();

    /**
     * 取り消しを行う
     *
     * @param canvas キャンバス
     */
    void undo(Canvas canvas);

    /**
     * 取り消しを行えるかどうかを取得する
     *
     * @return 取り消しを行えるかどうか
     */
    boolean canUndo();

    /**
     * やり直しを行う
     *
     * @param canvas キャンバス
     */
    void redo(Canvas canvas);

    /**
     * やり直しを行えるかどうかを取得する
     *
     * @return やり直しを行えるかどうか
     */
    boolean canRedo();

    /**
     * 以降追加される履歴を圧縮し、一つにまとめる
     * また、すでにstartCompressが呼ばれており、finishCompressが呼ばれていない時は、続けて追加する
     *
     * @param description 履歴の説明
     */
    void startCompress(String description);

    /**
     * 以降追加される履歴を圧縮し、一つにまとめる
     * また、すでにstartCompressが呼ばれており、finishCompressが呼ばれていない時は、続けて追加する
     *
     * @param descriptionSupplier finishCompressが呼ばれた際にgetメソッドが呼ばれ、履歴の説明が確定される
     */
    void startCompress(Supplier<String> descriptionSupplier);

    /**
     * 履歴の圧縮を終了する。
     * 以降追加される履歴はひとずつ追加される
     */
    void finishCompress();

}
