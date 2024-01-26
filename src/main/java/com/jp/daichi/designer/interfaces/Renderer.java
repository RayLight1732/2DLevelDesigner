package com.jp.daichi.designer.interfaces;

import java.awt.*;

/**
 * 描画用インターフェース
 */
public interface Renderer {
    /**
     * 描画を行う
     * @param g グラフィックオブジェクト
     */
    void draw(Graphics2D g);
}
