package com.jp.daichi.designer.interfaces;

import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * ツール
 */
public interface Tool extends ObservedObject {
    /**
     * マウスアダプターを取得する。常に同じインスタンスである必要がある
     *
     * @return マウスアダプター
     */
    MouseAdapter getMouseAdapter();

    /**
     * キャンバス上に描画を行う
     *
     * @param g グラフィックオブジェクト
     */
    void draw(Graphics2D g);

}
