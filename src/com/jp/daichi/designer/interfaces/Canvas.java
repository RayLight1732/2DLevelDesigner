package com.jp.daichi.designer.interfaces;

import java.util.List;

/**
 * デザイナーのキャンバス
 */
public interface Canvas {
    /**
     * レイヤーのリストのコピーを返す
     * @return レイヤーのリストのコピー
     */
    List<Layer> getLayers();

    /**
     * レイヤーを削除する
     * @param layer 対象のレイヤー
     * @return 削除に成功したかどうか
     */
    boolean removeLayer(Layer layer);

    /**
     * レイヤーを追加する
     * 名前が重複している場合、失敗
     * @param layer 対象のレイヤー
     * @return 追加に成功したかどうか
     */
    boolean addLayer(Layer layer);


}
