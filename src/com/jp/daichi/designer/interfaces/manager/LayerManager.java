package com.jp.daichi.designer.interfaces.manager;

import com.jp.daichi.designer.interfaces.DesignerObjectType;
import com.jp.daichi.designer.interfaces.Layer;

/**
 * レイヤーを管理する。
 */
public interface LayerManager extends IManager<Layer> {
    /**
     * 新しいレイヤーのインスタンスを作成する
     *
     * @param name 名前
     * @param type タイプ
     * @return 新しいレイヤーのインスタンス
     */
    Layer createInstance(String name, DesignerObjectType type);

    /**
     * 指定したタイプのデザイナーオブジェクトを管理するレイヤーを取得する
     *
     * @param type 管理するデザイナーオブジェクトのタイプ
     * @return 指定したタイプのデザイナーオブジェクトを管理するレイヤー
     */
    Layer getLayer(DesignerObjectType type);
}
