package com.jp.daichi.designer.interfaces;

import java.util.UUID;

/**
 * 画像表示用のオブジェクトを表すクラス
 */
public interface ImageObject extends DesignerObject {
    /**
     * マテリアルを設定する
     * @param uuid マテリアルのUUID
     */
    void setMaterialUUID(UUID uuid);

    /**
     * マテリアルを取得する
     * @return マテリアルのUUID
     */
    UUID getMaterialUUID();
}
