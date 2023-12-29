package com.jp.daichi.designer.interfaces;

import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * デザイナー用のオブジェクト
 */
public interface DesignerObject {
    /**
     * 座標を取得
     * @return 座標
     */
    Point getPosition();

    /**
     * 座標を設定
     * @param point 座標
     */
    void setPosition(Point point);

    /**
     * サイズを表すインスタンスのコピーを取得
     * @return 領域
     */
    Dimension getDimension();

    /**
     * 領域を設定
     * インスタンスはコピーされたうえで設定される
     * @param dimension 領域
     */
    void setDimension(Dimension dimension);

    /**
     * 描画の優先度を取得
     * @return 優先度
     */
    int getPriority();

    /**
     * 描画の優先度を設定
     * @param priority 優先度
     */
    void setPriority(int priority);

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
     * マウスの処理を行うマウスアダプターを取得する。
     * 常に同じインスタンスである必要がある。
     * @return マウスの処理を行うマウスアダプター
     */
    MouseAdapter getMouseAdapter();

    /**
     * 描画を行う
     * @param g グラフィックオブジェクト
     */
    void draw(Graphics2D g);
}
