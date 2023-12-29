package com.jp.daichi.designer.interfaces;

import java.util.List;

/**
 * 描画用レイヤー
 */
public interface Layer {
    /**
     * Z座標を取得
     * @return Z座標
     */
    int getZ();

    /**
     * このレイヤーに登録されているオブジェクトのリストを取得
     * @return このレイヤーに登録されているオブジェクトのリスト
     */
    List<DesignerObject> getObjects();

    /**
     * 描画されるかどうか
     * @return 描画されるかどうか
     */
    boolean isVisible();

    /**
     * 描画されるかどうかを設定
     * @param isVisible 描画されるか
     */
    void setVisible(boolean isVisible);

    /**
     * レイヤー名を取得する
     * @return レイヤー名
     */
    String getName();

    /**
     * レイヤー名を設定する
     * @param name レイヤー名
     */
    void setName(String name);
}
