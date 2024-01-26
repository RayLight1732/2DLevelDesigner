package com.jp.daichi.designer.interfaces.manager;

import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.DesignerObjectType;

/**
 * デザイナーオブジェクトのインスタンスの生成、管理を行う
 */
public interface DesignerObjectManager extends IManager<DesignerObject> {

    /**
     * デザイナーオブジェクトの新しいインスタンスを作成する
     *
     * @param name 名前
     * @param type タイプ
     * @param <T>  返り値の型
     * @return デザイナーオブジェクトの新しいインスタンス
     */
    <T extends DesignerObject> T createInstance(String name, DesignerObjectType type);

}