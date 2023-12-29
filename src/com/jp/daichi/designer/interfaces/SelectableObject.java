package com.jp.daichi.designer.interfaces;

/**
 * 選択、ドラッグができるオブジェクト
 */
public interface SelectableObject extends DesignerObject {
    /**
     * 選択できるかどうかを取得
     * @return 選択できるか
     */
    boolean isSelectable();

    /**
     * 選択されているかどうかを取得
     * @return 選択されているか
     */
    boolean isSelected();

    /**
     * 選択されているかどうかを設定
     * @param isSelected 選択されているか
     */
    void setIsSelected(boolean isSelected);

    /**
     * ドラッグされた時
     * @param to ドラッグされた目的地
     */
    void onDragged(Point to);
}
