package com.jp.daichi.designer.interfaces;

import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    /**
     * 選択用の枠線を取得する
     * @return 枠線
     */
    Frame getFrame();

    /**
     * 点上にあるデザイナーオブジェクトを取得する
     * @param point 点
     * @return 最前面にあるデザイナーオブジェクト
     */
    DesignerObject getDesignerObject(Point point);

    /**
     * 範囲内のデザイナーオブジェクトをすべて取得する
     * @param rectangle 領域
     * @return 範囲内デザイナーオブジェクト
     */
    Set<DesignerObject> getDesignerObjects(Rectangle rectangle);


    /**
     * 描画を行う
     * @param g グラフィックオブジェクト
     * @param width スクリーンの幅
     * @param height スクリーンの高さ
     */
    void draw(Graphics2D g,int width,int height);

    /**
     * オブザーバーを設定する
     * @param updateObserver オブザーバー
     */
    void setUpdateObserver(UpdateObserver updateObserver);

    /**
     * 描画領域を設定する
     * @param rectangle 描画領域
     */
    void setViewPort(Rectangle rectangle);

    /**
     * 描画領域を取得する
     * @return 描画領域
     */
    Rectangle getViewPort();

    /**
     * 視野角を設定する
     * @param angle 視野角(ラジアン)
     */
    void setPov(double angle);

    /**
     * 視野角を取得する
     * @return 視野角(ラジアン)
     */
    double getPov();

    /**
     * 空間内の座標をスクリーン上の座標に変換する
     * @param point 点
     * @param z z座標
     * @return スクリーン上の座標
     */
    Point convertToScreenPosition(Point point, double z);

    /**
     * スクリーン上の座標を空間内の座標に変換する
     * @param point 点
     * @param z z座標
     * @return 空間内の座標
     */
    Point convertFromScreenPosition(Point point,double z);

    /**
     * 縦、横の幅を設定し、それ用にTransformを更新する
     * このTransformは主にconvertFromScreenPositionやconvertToScreenPositionに用いられる
     * @param width 幅
     * @param height 高さ
     */
    void updateTransform(int width,int height);

    /**
     * マテリアルマネージャーを取得する
     * @return マテリアルマネージャー
     */
    MaterialManager getMaterialManager();
}
