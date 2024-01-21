package com.jp.daichi.designer.interfaces.manager;

import com.jp.daichi.designer.interfaces.ObservedObject;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * マネージャーの基底インターフェース
 * @param <T> 管理対象の型
 */
public interface IManager<T> extends ObservedObject {
    /**
     * インスタンスを取得する
     * @param uuid UUID
     * @return インスタンス
     */
    T getInstance(UUID uuid);


    /**
     * インスタンスの登録を解除する
     * @param object 登録解除するインスタンス
     * @return 登録解除に成功したらtrue
     */
    boolean removeInstance(T object);

    /**
     * 登録済みのインスタンスのリストのコピーを取得する
     * @return 登録済みインスタンスのリストのコピー
     */
    List<T> getAllInstances();

    /**
     * デシリアライズを行うと同時に登録を行う。
     * @param map シリアライズされたデータ
     * @return 新しいT型のインスタンス
     */
    //TODO UUIDの重複があった場合
    T deserializeManagedObject(Map<String,Object> map);

    /**
     * 名前の重複や空白の名前が生じないように名前を変更する
     * @param uuid 対象のUUID nullでないとき、重複している対象のuuidが、このuuidと一致したら元の名前を返す
     * @param name 元の名前
     * @return 名前の重複や空白の名前が生じないように変更された名前
     */
    String resolveName(UUID uuid,String name);
}
