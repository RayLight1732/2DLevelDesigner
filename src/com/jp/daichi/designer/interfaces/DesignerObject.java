package com.jp.daichi.designer.interfaces;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.geom.Rectangle2D;
import java.util.UUID;

/**
 * デザイナー用のオブジェクト
 */
public interface DesignerObject extends Comparable<DesignerObject>,ObservedObject {
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
     * サイズを取得
     * @return サイズ
     */
    SignedDimension getDimension();

    /**
     * サイズを設定
     * @param dimension サイズ
     */
    void setDimension(SignedDimension dimension);

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


    Canvas getCanvas();

    /**
     * このオブジェクトを選択できるかどうか
     * @return 選択できるかどうか
     */
    boolean isSelectable();

    /**
     * Z座標(奥行)を取得する
     * @return Z座標
     */
    double getZ();

    /**
     * Z座標(奥行)を指定する
     * @param z Z座標
     */
    void setZ(double z);

    /**
     * 名前を取得する
     * @return 名前
     */
    String getName();

    /**
     * 名前を設定する
     * @param name 名前
     */
    void setName(String name);

    /**
     * このオブジェクトのUUIDを取得する
     * @return UUID
     */
    UUID getUUID();

    /**
     * このオブジェクトが存在する領域を取得する
     * @return 領域
     */
    Rectangle2D getRectangle();

    /**
     * デザイナーオブジェクトのタイプを取得する
     * @return デザイナーオブジェクトのタイプ
     */
    DesignerObjectType getType();

}
