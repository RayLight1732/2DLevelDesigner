package com.jp.daichi.designer.interfaces.editor;

import java.util.List;

/**
 * 履歴を管理する
 */
public interface History {
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
}
