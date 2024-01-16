package com.jp.daichi.designer.interfaces;

import java.util.List;
import java.util.UUID;

public interface MaterialManager {
    /**
     * マテリアルを取得する
     * @param uuid UUID
     * @return マテリアル
     */
    Material getMaterial(UUID uuid);

    /**
     * マテリアルを登録する
     * @param material マテリアル
     * @return 登録に成功したらtrue
     */
    boolean registerMaterial(Material material);

    /**
     * マテリアルの登録を解除する
     * @param material 登録解除するマテリアル
     * @return 登録解除に成功したらtrue
     */
    boolean removeMaterial(Material material);

    /**
     * 登録済みのマテリアルのリストのコピーを取得する
     * @return 登録済みマテリアルのリストのコピー
     */
    List<Material> getMaterials();
}
