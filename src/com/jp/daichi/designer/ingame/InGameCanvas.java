package com.jp.daichi.designer.ingame;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.interfaces.manager.LayerManager;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;
import com.jp.daichi.designer.simple.DeserializeUtil;
import com.jp.daichi.designer.simple.SimpleCanvas;

import java.util.Map;

/**
 * ゲーム用のキャンバスの実装
 */
public class InGameCanvas extends SimpleCanvas {

    /**
     * デシリアライズを行う
     * @param materialManager マテリアルマネージャー
     * @param layerManager レイヤーマネージャー
     * @param designerObjectManager デザイナーオブジェクトマネージャー
     * @param serialized シリアライズされたデータ
     * @return デシリアライズの結果
     */
    public static Canvas deserialize(MaterialManager materialManager, LayerManager layerManager, DesignerObjectManager designerObjectManager, Map<String,Object> serialized) {
        InGameCanvas canvas = new InGameCanvas(materialManager, layerManager, designerObjectManager);
        try {
            DeserializeUtil.setCanvasProperties(canvas,serialized);
            return canvas;
        } catch (ClassCastException|NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新しいキャンバスのインスタンスを作成する
     *
     * @param materialManager       マテリアルマネージャー
     * @param layerManager          レイヤーマネージャー
     * @param designerObjectManager デザイナーオブジェクトマネージャー
     */
    public InGameCanvas(MaterialManager materialManager, LayerManager layerManager, DesignerObjectManager designerObjectManager) {
        super(materialManager, layerManager, designerObjectManager);
    }
}
