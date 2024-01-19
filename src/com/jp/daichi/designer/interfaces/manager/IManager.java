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
     * インスタンスを登録する
     * 名前がかぶっていた場合変更されることがある
     * @param name 名前
     * @return 新しいインスタンス
     */
    T createInstance(String name);

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
}
