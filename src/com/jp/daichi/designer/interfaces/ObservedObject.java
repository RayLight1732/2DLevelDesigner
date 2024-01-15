package com.jp.daichi.designer.interfaces;

/**
 * 監視され、何らかのプロパティが変更されたときにオブザーバーのUpdateを呼び出すオブジェクトに付けられるインターフェース
 */
public interface ObservedObject {
    /**
     * このオブジェクトに紐づけられているオブザーバーを取得する
     * @return オブザーバー
     */
    UpdateObserver getUpdateObserver();

    /**
     * このオブジェクトにオブザーバーを紐づける
     * @param observer オブザーバー
     */
    void setUpdateObserver(UpdateObserver observer);
}
