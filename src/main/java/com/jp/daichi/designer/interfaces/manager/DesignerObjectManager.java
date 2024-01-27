package com.jp.daichi.designer.interfaces.manager;

import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.DesignerObjectType;

import java.util.Map;

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

    /**
     * デシリアライズを行うと同時に登録を行う。
     *
     * @param map シリアライズされたデータ
     * @return 新しいT型のインスタンス
     */
    //TODO UUIDの重複があった場合
    DesignerObject deserializeManagedObject(Map<String, Object> map,boolean resolveUUIDDuplication);

}
