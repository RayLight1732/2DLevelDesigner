package com.jp.daichi.designer.interfaces;

import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.interfaces.manager.LayerManager;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * デザイナーのキャンバス
 */
public interface Canvas extends ObservedObject {
    /**
     * レイヤーのUUIDのリストのコピーを返す
     *
     * @return レイヤーのUUIDのリストのコピー
     */
    List<UUID> getLayers();

    /**
     * レイヤーを削除する
     *
     * @param layerUUID 対象のレイヤーのUUID
     * @return 削除に成功したかどうか
     */
    boolean removeLayer(UUID layerUUID);

    /**
     * レイヤーを追加する
     *
     * @param layerUUID 対象のレイヤーのUUID
     * @return 追加に成功したかどうか
     */
    boolean addLayer(UUID layerUUID);


    /**
     * 点上にあるデザイナーオブジェクトを取得する
     *
     * @param point 点
     * @return 最前面にあるデザイナーオブジェクト
     */
    DesignerObject getDesignerObject(Point point);

    /**
     * 範囲内のデザイナーオブジェクトをすべて取得する
     *
     * @param rectangle 領域
     * @return 範囲内デザイナーオブジェクト
     */
    Set<DesignerObject> getDesignerObjects(Rectangle rectangle);


    /**
     * 描画を行う
     *
     * @param g      グラフィックオブジェクト
     * @param width  スクリーンの幅
     * @param height スクリーンの高さ
     */
    void draw(Graphics2D g, int width, int height);

    /**
     * 背景の描画(デザイナーオブジェクト含む)が終わった後に呼ばれるレンダラーを追加する
     * @param renderer レンダラー
     */
    void addRenderer(Renderer renderer);

    /**
     * 背景の描画(デザイナーオブジェクト含む)が終わった後に呼ばれるレンダラーを削除する
     * @param renderer レンダラー
     * @return 削除に成功したらtrue
     */
    boolean removeRenderer(Renderer renderer);

    /**
     * オブザーバーを設定する
     *
     * @param updateObserver オブザーバー
     */
    void setUpdateObserver(UpdateObserver updateObserver);

    /**
     * 描画領域を設定する
     *
     * @param rectangle 描画領域
     */
    void setViewport(Rectangle rectangle);

    /**
     * 描画領域を取得する
     *
     * @return 描画領域
     */
    Rectangle getViewport();

    /**
     * 視野角を設定する
     *
     * @param angle 視野角(ラジアン)
     */
    void setPov(double angle);

    /**
     * 視野角を取得する
     *
     * @return 視野角(ラジアン)
     */
    double getPov();

    /**
     * 空間内の座標をスクリーン上の座標に変換する
     *
     * @param point 点
     * @param z     z座標
     * @return スクリーン上の座標
     */
    Point convertToScreenPosition(Point point, double z);

    /**
     * スクリーン上の座標を空間内の座標に変換する
     *
     * @param point 点
     * @param z     z座標
     * @return 空間内の座標
     */
    Point convertFromScreenPosition(Point point, double z);

    /**
     * スクリーン上の座標を空間内の座標に変換する
     *
     * @param point      点
     * @param z          z座標
     * @param inViewport 与えられた点がビューポートを基準としているか、UIを基準としているか
     * @return 空間内の座標
     */
    Point convertFromScreenPosition(Point point, double z, boolean inViewport);

    /**
     * 縦、横の幅を設定し、それ用にTransformを更新する
     * このTransformは主にconvertFromScreenPositionやconvertToScreenPositionに用いられる
     *
     * @param width  幅
     * @param height 高さ
     */
    void updateTransform(int width, int height);

    /**
     * マテリアルマネージャーを取得する
     *
     * @return マテリアルマネージャー
     */
    MaterialManager getMaterialManager();

    /**
     * レイヤーマネージャーを取得する
     *
     * @return レイヤーマネージャー
     */
    LayerManager getLayerManager();

    /**
     * デザイナーオブジェクトマネージャーを取得する
     *
     * @return デザイナーオブジェクトマネージャー
     */
    DesignerObjectManager getDesignerObjectManager();

    /**
     * 背景画像用のマテリアルを設定する
     *
     * @param uuid マテリアルのUUID
     */
    void setMaterialUUID(UUID uuid);

    /**
     * 背景画像用のマテリアルを取得する
     *
     * @return マテリアルのUUID
     */
    UUID getMaterialUUID();

    /**
     * 霧の色を取得する
     *
     * @return 霧の色
     */
    Color getFogColor();

    /**
     * 霧の色を設定する
     *
     * @param color 霧の色
     */
    void setFogColor(Color color);

    /**
     * 霧の強さを取得する
     *
     * @return 霧の強さ
     */
    double getFogStrength();

    /**
     * 霧の強さを設定する
     *
     * @param fogStrength 霧の強さ
     */
    void setFogStrength(double fogStrength);
}
