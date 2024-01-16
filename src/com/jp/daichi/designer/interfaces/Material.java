package com.jp.daichi.designer.interfaces;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

/**
 * イメージオブジェクト用のマテリアルデータ
 */
public interface Material extends ObservedObject {

    /**
     * このマテリアルの名前を取得する
     * @return 名前
     */
    String getName();

    /**
     * このマテリアルの名前を設定する
     * @param name 名前
     */
    void setName(String name);

    /**
     * UUIDを取得する
     * @return UUID
     */
    UUID getUUID();

    /**
     * 画像を取得
     * @return 画像
     */
    BufferedImage getImage();

    /**
     * 画像を設定
     * @param image 画像
     */
    void setImage(BufferedImage image);

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
    SignedDimension getUVDimension();

    /**
     * UVの描画領域を設定
     * インスタンスはコピーされて設定される
     * @param dimension 描画領域
     */
    void setUVDimension(SignedDimension dimension);


}
