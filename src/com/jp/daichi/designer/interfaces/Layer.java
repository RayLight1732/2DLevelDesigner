package com.jp.daichi.designer.interfaces;

import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;

import java.awt.*;
import java.util.List;
import java.util.UUID;

/**
 * 描画用レイヤー
 */
public interface Layer extends ObservedObject {



    /**
     * このレイヤーに登録されているオブジェクトのUUIDのリストのコピーを取得
     * ソートされている必要がある
     * @return このレイヤーに登録されているオブジェクトのリストのコピー
     */
    List<UUID> getObjects();

    /**
     * デザイナーオブジェクトをこのレイヤーに追加する
     * @param designerObjectUUID 追加するデザイナーオブジェクトのUUID
     */
    void add(UUID designerObjectUUID);

    /**
     * デザイナーオブジェクトをこのレイヤーから削除する
     * @param designerObjectUUID 削除するデザイナーオブジェクトのUUID
     * @return 削除するに成功したかどうか
     */
    boolean remove(UUID designerObjectUUID);

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
     * @param designerObjectManager デザイナーオブジェクトマネージャー
     */
    void draw(Graphics2D g,DesignerObjectManager designerObjectManager);

    /**
     * このレイヤーのUUIDを取得する
     * @return UUID
     */
    UUID getUUID();
}
