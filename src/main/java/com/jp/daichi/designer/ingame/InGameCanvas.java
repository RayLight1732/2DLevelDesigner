package com.jp.daichi.designer.ingame;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.DesignerObjectType;
import com.jp.daichi.designer.interfaces.Layer;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.interfaces.manager.LayerManager;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;
import com.jp.daichi.designer.simple.DeserializeUtil;
import com.jp.daichi.designer.simple.SimpleCanvas;

import java.awt.*;
import java.util.Map;
import java.util.Objects;

/**
 * ゲーム用のキャンバスの実装
 */
public class InGameCanvas extends SimpleCanvas {

    /**
     * デシリアライズを行う
     *
     * @param materialManager       マテリアルマネージャー
     * @param layerManager          レイヤーマネージャー
     * @param designerObjectManager デザイナーオブジェクトマネージャー
     * @param serialized            シリアライズされたデータ
     * @return デシリアライズの結果
     */
    public static Canvas deserialize(MaterialManager materialManager, LayerManager layerManager, DesignerObjectManager designerObjectManager, Map<String, Object> serialized) {
        InGameCanvas canvas = new InGameCanvas(materialManager, layerManager, designerObjectManager);
        try {
            DeserializeUtil.setCanvasProperties(canvas, serialized);
            return canvas;
        } catch (ClassCastException | NullPointerException e) {
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

    @Override
    protected void drawLayer(Graphics2D g) {
        getLayers().stream().map(uuid -> getLayerManager().getInstance(uuid))
                .filter(Objects::nonNull)
                .filter(Layer::isVisible)
                .filter(layer -> layer.getObjectType() == DesignerObjectType.IMAGE)
                .forEach(layer -> layer.draw(g, getDesignerObjectManager()));
    }
}
