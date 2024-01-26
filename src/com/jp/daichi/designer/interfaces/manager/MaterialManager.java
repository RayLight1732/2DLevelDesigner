package com.jp.daichi.designer.interfaces.manager;

import com.jp.daichi.designer.interfaces.Material;


public interface MaterialManager extends IManager<Material> {

    /**
     * インスタンスを登録する
     * 名前がかぶっていた場合変更されることがある
     *
     * @param name 名前
     * @return 新しいインスタンス
     */
    Material createInstance(String name);
}
