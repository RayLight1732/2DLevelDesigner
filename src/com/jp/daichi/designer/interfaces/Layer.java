package com.jp.daichi.designer.interfaces;

import java.awt.*;
import java.util.List;

/**
 * 描画用レイヤー
 */
public interface Layer extends ObservedObject {



    /**
     * このレイヤーに登録されているオブジェクトのリストのコピーを取得
     * ソートされている必要がある
     * @return このレイヤーに登録されているオブジェクトのリストのコピー
     */
    List<DesignerObject> getObjects();

    /**
     * デザイナーオブジェクトをこのレイヤーに追加する
     * @param designerObject 追加するデザイナーオブジェクト
     */
    void add(DesignerObject designerObject);

    /**
     * デザイナーオブジェクトをこのレイヤーから削除する
     * @param designerObject 削除するデザイナーオブジェクト
     * @return 削除するに成功したかどうか
     */
    boolean remove(DesignerObject designerObject);

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

    /**
     * 描画を行う
     * @param g グラフィックオブジェクト
     */
    void draw(Graphics2D g);
}
