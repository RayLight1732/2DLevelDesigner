package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.interfaces.Canvas;

import java.awt.*;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * シリアライズのヘルパークラス
 */
public class DeserializeUtil {

    public static final String POV = "Pov";
    public static final String VIEWPORT = "ViewPort";
    public static final String FOG_STRENGTH = "FogStrength";
    public static final String FOG_COLOR = "FogColor";
    public static final String MATERIAL_UUID = "MaterialUUID";

    /**
     * シリアライズされたデータからプロパティを読み込み、キャンバスのインスタンスに設定する
     *
     * @param canvas     キャンバスのインスタンス
     * @param serialized シリアライズされたデータ
     */
    public static void setCanvasProperties(Canvas canvas, Map<String, Object> serialized) throws ClassCastException, NullPointerException {
        Double pov = (Double) serialized.get(POV);
        if (pov != null) {
            canvas.setPov(pov);
        }
        Rectangle viewPort = (Rectangle) serialized.get(VIEWPORT);
        Objects.requireNonNull(viewPort);
        canvas.setViewport(viewPort);
        Double fogStrength = (Double) serialized.get(FOG_STRENGTH);
        if (fogStrength != null) {
            canvas.setFogStrength(fogStrength);
        }
        Color color = (Color) serialized.get(FOG_COLOR);
        canvas.setFogColor(color);
        UUID materialUUID = (UUID) serialized.get(MATERIAL_UUID);
        canvas.setMaterialUUID(materialUUID);
    }

}
