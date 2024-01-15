package com.jp.daichi.designer.interfaces;


import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 選択された物の枠線を表す
 */
public interface Frame extends DesignerObject {
    /**
     * 選択されたデザイナーオブジェクトのセットのコピーを返す
     * @return 選択されたデザイナーオブジェクトのセットのコピー
     */
    Set<DesignerObject> getSelected();

    /**
     * デザイナーオブジェクトを選択する
     * nullが引数として渡されたときはfalseを返す
     * @param designerObject 対象のデザイナーオブジェクト
     * @return 追加できたときtrue
     */
    boolean addSelectedObject(DesignerObject designerObject);

    /**
     * 全てのデザイナーオブジェクトを選択する
     * @param designerObjects 対象のデザイナーオブジェクト群
     */
    void addAll(Collection<DesignerObject> designerObjects);

    /**
     * デザイナーオブジェクトの選択を解除する
     * nullが引数として渡されたときはfalseを返す
     * @param designerObject 対象のデザイナーオブジェクト
     * @return 削除できたときtrue
     */
    boolean removeSelectedObject(DesignerObject designerObject);

    /**
     * 全ての選択を解除する
     */
    void clearSelectedObject();

    /**
     * 選択されたデザイナーオブジェクトの数を取得する
     * @return 選択されたデザイナーオブジェクトの数
     */
    int getSelectedObjectCount();

    /**
     * 位置と大きさを設定する。選択されたオブジェクトの位置、大きさも適切に設定される
     * @param point 位置
     * @param dimension 大きさ
     */
    void setPositionAndDimension(Point point,SignedDimension dimension);
}
