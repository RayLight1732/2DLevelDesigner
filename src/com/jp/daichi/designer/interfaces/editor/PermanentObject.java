package com.jp.daichi.designer.interfaces.editor;

import com.jp.daichi.designer.interfaces.ObservedObject;

import java.util.Map;

/**
 * 更新が行われた際にデータが保存されるオブジェクトに付けられる
 */
public interface PermanentObject {
    /**
     * シリアライズを行う
     * @return シリアライズされたデータ
     */
    Map<String,Object> serialize();

    /**
     * 作業履歴を保存するかどうかを取得する
     * @return 保存するならtrue
     */
    boolean saveHistory();

    /**
     * 作業履歴を保存するかどうかを設定する
     * @param saveHistory 作業履歴を保存するか
     */
    void setSaveHistory(boolean saveHistory);
}
