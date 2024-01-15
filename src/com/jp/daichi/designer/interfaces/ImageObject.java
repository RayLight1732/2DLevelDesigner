package com.jp.daichi.designer.interfaces;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 画像表示用のオブジェクトを表すクラス
 */
public interface ImageObject extends DesignerObject {
    /**
     * 画像を取得
     * @return 画像
     */
    BufferedImage getImage();

    /**
     * UV座標を取得
     * @return UV座標
     */
    Point getUV();

    /**
     * UV座標を設定
     * @param point UV座標
     */
    void setUV(Point point);

    /**
     * UVの描画領域を表すオブジェクトのコピーを取得
     * @return UVの描画領域
     */
    Dimension getUVDimension();

    /**
     * UVの描画領域を設定
     * インスタンスはコピーされて設定される
     * @param dimension 描画領域
     */
    void setUVDimension(Dimension dimension);
}
