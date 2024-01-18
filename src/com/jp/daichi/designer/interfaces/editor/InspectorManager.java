package com.jp.daichi.designer.interfaces.editor;

import javax.swing.*;

/**
 * Inspector画面に表示されるコンポーネントの生成を行う
 */
public interface InspectorManager {
    /**
     * 与えられたオブジェクトのInspector画面を作成する
     * @param object 対象のオブジェクト
     * @return Inspector画面 存在しない場合はnull
     */
    JComponent createInspectorView(Object object);
}
